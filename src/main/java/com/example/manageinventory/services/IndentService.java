package com.example.manageinventory.services;

import com.example.manageinventory.authentication.AuthenticationService;
import com.example.manageinventory.constants.InventoryConstants;
import com.example.manageinventory.models.*;
import com.example.manageinventory.repositories.*;
import com.example.manageinventory.view_models.IndentLineViewModel;
import com.example.manageinventory.view_models.IndentViewModel;
import com.example.manageinventory.view_models.ReturnIndentViewModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class IndentService implements InitializingBean {
    @Autowired
    private IndentRepository indentRepository;
    @Autowired
    private ReturnIndentRepository returnIndentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private IndentLineRepository indentLineRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private AuthenticationService authenticationService;


    public static Map<String, String> validationErrorObj =  new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }

    /**
     * Get list of Indents
     * @return
     */
    public ResponseEntity getListOfIndents(){
        List<Indent> indentList = this.indentRepository.findAll();

        List<IndentViewModel> indentViewModelList = new ArrayList<>();
        for(Indent indent: indentList){
            indentViewModelList.add(mapToIndentViewModel(indent));
        }

        return ResponseEntity.status(HttpStatus.OK).body(indentViewModelList);
    }

    /**
     * Get details of Indent Record by Id
     * @param id
     * @return
     */
    public ResponseEntity getIndentById(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent Not Found for ID: %d", id));
        };
        return ResponseEntity.status(HttpStatus.OK).body(mapToIndentViewModel(indent));
    }

    /**
     * Create a new Indent
     * @param indentViewModel
     * @return
     * @throws ParseException
     */
    @Transactional
    public ResponseEntity createNewIndent(IndentViewModel indentViewModel) throws ParseException {

        Indent indent = new Indent();

        Date deliveryDate= new SimpleDateFormat(InventoryConstants.DefaultDateFormat).parse(indentViewModel.getDeliveryDate());
        indent.setDeliveryDate(deliveryDate);
        indent.setType(indentViewModel.getType());
        indent.setRemarks(indentViewModel.getRemarks());
        //Check and add raisedBy user
        User raisedBy = authenticationService.getLoggedInUserObject();
        indent.setRaisedBy(raisedBy);
        //Set status based on type
        if(indent.getType() == IndentType.INCOMING){
            indent.setStatus(IndentStatus.ORDER_PLACED);
        }else{
            indent.setStatus(IndentStatus.ORDER_RECEIVED);
        }

        //Save the indent so that we can create indent lines for the persisted indent record
        indent = indentRepository.saveAndFlush(indent);
        //Validate and Set Indent Lines
        if (!validateIndentViewLines(indentViewModel.getIndentLineList(), indent)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorObj);
        }
        //Only when a valid location id is sent, proceed with location-product mapping
        if(indentViewModel.getLocation_id() > 0){
            if(!validateAndSetLocation(indentViewModel.getLocation_id(), indent, true)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorObj);
            }
        }

        indentRepository.saveAndFlush(indent);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToIndentViewModel(indent));
    }

    //Validate Line items and add them to indent
    private Boolean validateIndentViewLines(Set<IndentLineViewModel> indentLineViewModels, Indent indent){
        if(indentLineViewModels.size() <= 0){
            setValidationErrorObject("Validation", "Atleast one product must be present while creating Indent",
                    "Indent", "IndentLines");
            return false;
        }

        //Go through each incoming indentline and cross check if the product exists or not. If not return error
        // return error if any of the product gets mismatched
        for(IndentLineViewModel indentLineViewModel: indentLineViewModels){
            //Check if there is a product for the given ID
            Product registeredProduct = productRepository.findProductById(indentLineViewModel.getProduct_id());
            if(registeredProduct == null || indentLineViewModel.getQuantity() <= 0){
                setValidationErrorObject("Validation", String.format("Either there is no product with ID :%d or the quantity specificed is <= 0", indentLineViewModel.getProduct_id()),
                        "Indent", "IndentLines");
                return false;
            }

            IndentLine indentLine = new IndentLine();
            indentLine.setUnitPrice(indentLineViewModel.getUnitPrice());
            indentLine.setQuantity(indentLineViewModel.getQuantity());
            indentLine.setProduct(registeredProduct);
            indentLine.setIndent(indent);

            indentLine = indentLineRepository.saveAndFlush(indentLine);
            indent.addIndentLine(indentLine);
        }
        return true;
    }

    //Validate Location and set the indent's and its products location
    private Boolean validateAndSetLocation(int location_id, Indent indent, Boolean updateProductQuantity) {
        //Get the location from id
        Location location = locationRepository.findLocationById(location_id);
        if(location == null){
            setValidationErrorObject("Validation", String.format("Location not found for id :%d",location_id),
                    "Indent", "Location");
            return false;
        }
        double totalPrice = 0;
        //Populate all product's
        for(IndentLine indentLine: indent.getIndentLineList()){

            Product product = indentLine.getProduct();
            product.getLocations().add(location);
            //Update product quantity and price only during indent creation.
            if(updateProductQuantity) {
                totalPrice += indentLine.getUnitPrice() * indentLine.getQuantity();
                if (indent.getType() == IndentType.INCOMING) {
                    product.setQuantity(product.getQuantity() + indentLine.getQuantity());
                } else {
                    product.setQuantity(product.getQuantity() - indentLine.getQuantity());
                }
            }

            productRepository.saveAndFlush(product);
            //TODO: Need to populate indent_id and quantity to this "location_product" table. For this need to have additional properties to many to many relation.
        }
        //Set total price during creation only
        if(updateProductQuantity) {
            indent.setTotalPrice(totalPrice);
        }
        indent.setLocation(location);
        return true;
    }

    /**
     * Update Indent details. Only status, remarks, delivery, location date is updatable
     * @param id
     * @param indentViewModel
     * @return
     * @throws ParseException
     */
    @Transactional
    public ResponseEntity updateIndentDetails(int id, IndentViewModel indentViewModel) throws ParseException {

        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }

        List<String> indentStatusArray = Arrays.asList(IndentStatus.DELETED.toString(), IndentStatus.DISPATCHED.toString(), IndentStatus.PROCESSED.toString());

        //Deleted, dispatched or processed indents cannot be updated to any other status
        if(indentStatusArray.contains(indent.getStatus().toString())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Indent has been marked as %s and hence cannot be updated further", indent.getStatus().toString()));
        }
        //Incoming Indent's status can only be updated to "Processed" and outgoing indent's status can only be updated to "Dispatched
        if((indentViewModel.getStatus()!= null && indentViewModel.getStatus() != indent.getStatus()) &&
                (indent.getStatus() == IndentStatus.ORDER_PLACED && indentViewModel.getStatus() != IndentStatus.PROCESSED)
                || (indent.getStatus() == IndentStatus.ORDER_RECEIVED && indentViewModel.getStatus() != IndentStatus.DISPATCHED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Indent has been marked as %s and cannot be updated as %s",indent.getStatus().toString(), indentViewModel.getStatus().toString()));
        }

        //Allow updating status, location, remarks, delivery date columns only
        Date deliveryDate= new SimpleDateFormat(InventoryConstants.DefaultDateFormat).parse(indentViewModel.getDeliveryDate());
        indent.setDeliveryDate(deliveryDate);
        indent.setStatus(indentViewModel.getStatus());
        //Allow updating Location but not updating indent lines
        //Only when a valid location id is sent, proceed with location-product mapping
        if(indentViewModel.getLocation_id() > 0){
            if(!validateAndSetLocation(indentViewModel.getLocation_id(), indent, false)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorObj);
            }
        }
        indentRepository.saveAndFlush(indent);
        return ResponseEntity.status(HttpStatus.OK).body(mapToIndentViewModel(indent));
    }

    /**
     * Delete Indent and mark its status as "Deleted"
     * @param id
     * @return
     */
    @Transactional
    public ResponseEntity markIndentAsDeleted(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }
        if(indent.getStatus() == IndentStatus.DELETED){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Indent ID: %d is already Deleted.",id));
        }

        indent.setStatus(IndentStatus.DELETED);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Indent %d has been marked as Deleted", indent.getId()));
    }

    /**
     * Get Location for a given indent. currently a single location can be applied to an indent
     * @param id
     * @return
     */
    @Transactional
    public ResponseEntity getLocationsForIndent(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(locationService.mapToLocationView(indent.getLocation()));
    }

    @Transactional
    public ResponseEntity createReturnForIndent(int id, ReturnIndentViewModel returnIndentViewModel) {

        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Parent Indent with ID: %d not found",id));
        }
        ReturnIndent returnIndent = new ReturnIndent();

        //Validate multiple returns for this parent indent so that cumulative of all returns shouldn't exceed parent indetn's quantitt for each product
        if(!validatePreviousReturnIndentLines(indent, returnIndentViewModel.getIndentLineList())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorObj);
        }

        //If indent type is incoming, then create return of type incoming
        if(indent.getType() == IndentType.INCOMING){
            returnIndent.setType(ReturnIndentType.RETURN_INCOMING);
            returnIndent.setStatus(ReturnIndentStatus.INCOMING_RETURN_CREATED);
        }else{
            returnIndent.setType(ReturnIndentType.RETURN_OUTGOING);
            returnIndent.setStatus(ReturnIndentStatus.OUTGOING_RETURN_CREATED);
        }
        //All fine, now create the return indent, also credit/debit products in the inventory
        returnIndent.setIndent(indent);
        returnIndent.setReturnDate(new Date());
        returnIndent.setRemarks(returnIndentViewModel.getRemarks());

        returnIndentRepository.saveAndFlush(returnIndent);


        //Check the product list and quantities and make sure no other products or quantity exceeding the original indent are being returned
        if(returnIndentViewModel.getIndentLineList() == null || returnIndentViewModel.getIndentLineList().size() <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Return Indent Line should atleast contain 1 product from the original indent."));
        }else{
            //Iterate through each incoming indent view lines and prepare return indent line
            Set<IndentLine> returnIndentLines = new HashSet<>();
            //Go through each incoming indentline and cross check with existing indent's indentlines and
            // return error if any of the product gets mismatched
            for(IndentLineViewModel returnIndentLineViewModel: returnIndentViewModel.getIndentLineList()){

                //All fine. Create and Assign a new IndentLine the quantity and price from return indent view model and assign the line to return indent
                IndentLine newIndentLine = new IndentLine();
                newIndentLine.setQuantity(returnIndentLineViewModel.getQuantity());
                newIndentLine.setUnitPrice(returnIndentLineViewModel.getUnitPrice());
                newIndentLine.setReturnIndent(returnIndent);
                newIndentLine.setProduct(productRepository.findProductById(returnIndentLineViewModel.getProduct_id()));

                returnIndentLines.add(newIndentLine);
            }

            //Save return indent line items
            indentLineRepository.saveAll(returnIndentLines);
            //Add all the return indent lines prepared above
            returnIndent.setIndentLineList(returnIndentLines);
            //Save the return indent
            returnIndentRepository.saveAndFlush(returnIndent);


            //Now update product's quantity based on the returned line items
            for(IndentLine returnIndentLine: returnIndent.getIndentLineList()){
                Product product = returnIndentLine.getProduct();

                int existingQuantity = product.getQuantity();
                if(returnIndent.getType() == (ReturnIndentType.RETURN_INCOMING)){
                    product.setQuantity(existingQuantity - returnIndentLine.getQuantity());
                }else{
                    product.setQuantity(existingQuantity + returnIndentLine.getQuantity());
                }
                this.productRepository.saveAndFlush(product);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToReturnIndentView(returnIndent));
        }

    }

    private boolean validatePreviousReturnIndentLines(Indent indent, Set<IndentLineViewModel> returnIndentViewModels) {
        Set<IndentLine> parentIndentLineList = indent.getIndentLineList();
        //TODO> since indent.getIndentLines is not returning all the child lines, getting the indentlines directly from db.
        // To be investigated further
        if(parentIndentLineList.size() == 0){
            parentIndentLineList = indentLineRepository.getAllIndentLinesForIndent(indent.getId());
        }
        Map<Integer, Integer> masterProductQuantityMapParentIndent = new HashMap<>();
        for(IndentLine parentIndentLine: parentIndentLineList) {
            //Check and populate map data for each product type
            if (masterProductQuantityMapParentIndent.get(parentIndentLine.getProduct().getId()) == null) {
                masterProductQuantityMapParentIndent.put(parentIndentLine.getProduct().getId(), parentIndentLine.getQuantity());
            } else {
                Integer currentQuantity = masterProductQuantityMapParentIndent.get(parentIndentLine.getProduct().getId());
                masterProductQuantityMapParentIndent.put(parentIndentLine.getProduct().getId(), currentQuantity + parentIndentLine.getQuantity());
            }
        }


        //Check if there is any issue with the product list
        for(IndentLineViewModel returnIndentLineViewModel: returnIndentViewModels){
            IndentLine parentIndentLineFound = new IndentLine();
            //Go through each of the indent's indentlines and see if the product is present or not
            for(IndentLine parentIndentLine: parentIndentLineList){

                if(parentIndentLine.getProduct().getId() == returnIndentLineViewModel.getProduct_id()){
                    parentIndentLineFound = parentIndentLine;
                    break;
                }
            }
            //If no such indent line found, return error
            if (parentIndentLineFound.getProduct() == null){
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product ID %d not found within the original indent %d ",returnIndentLineViewModel.getProduct_id(), indent.getId()));
                setValidationErrorObject("ValidationError", String.format("Product ID %d not found within the original indent %d ",returnIndentLineViewModel.getProduct_id(), indent.getId()), "ReturnIndent", "IndentLine");
                return false;
            }
            //If quantity is <=0 return error
            if( returnIndentLineViewModel.getQuantity() <= 0){
                setValidationErrorObject("ValidationError", String.format("Please specify atleast 1 quantity of product %d", parentIndentLineFound.getProduct().getId()), "ReturnIndent", "IndentLine");
                return false;
            }
            //If relevant product found, make sure that the quantity is <= original quantity
            if(parentIndentLineFound.getQuantity() < returnIndentLineViewModel.getQuantity()){
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("You can only return %d items for the product %d ",parentIndentLineFound.getQuantity(), parentIndentLineFound.getProduct().getId() ));
                setValidationErrorObject("ValidationError", String.format("You can only return %d items for the product %d ",parentIndentLineFound.getQuantity(), parentIndentLineFound.getProduct().getId()), "ReturnIndent", "IndentLine");
                return false;
            }
            //If relevant product found, make sure that the price is unchanged
            if(parentIndentLineFound.getUnitPrice() != returnIndentLineViewModel.getUnitPrice()){
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Price mismatch while returning product %d. Original price: %d, New Price: %d ",parentIndentLineFound.getProduct().getId(), parentIndentLineFound.getUnitPrice(), returnIndentLineViewModel.getUnitPrice() ));
                setValidationErrorObject("ValidationError", String.format("Price mismatch while returning product %d. Original price: %f, New Price: %f ",parentIndentLineFound.getProduct().getId(), parentIndentLineFound.getUnitPrice(), returnIndentLineViewModel.getUnitPrice()), "ReturnIndent", "IndentLine");
                return false;
            }

        }


        //Get all previous return Indents
        Set<ReturnIndent> returnIndents = returnIndentRepository.getAllReturnIndentsForIndent(indent.getId());
        //Prepare a master list of all indent lines for each product type
        Map<Integer, Integer> masterProductQuantityMapReturnIndent = new HashMap<>();

        for(ReturnIndent returnIndent: returnIndents){
            //TODO Getting indentlines explicitly
            //For each return indent's indeline line add relevant info into master indent line(each object represents a unique product
            Set<IndentLine> returnIndentLines = indentLineRepository.getAllIndentLinesForReturnIndent(returnIndent.getId());
            for(IndentLine returnIndentLine: returnIndentLines){
                //Check and populate map data for each product type
                if(masterProductQuantityMapReturnIndent.get(returnIndentLine.getProduct().getId()) == null){
                    masterProductQuantityMapReturnIndent.put(returnIndentLine.getProduct().getId(), returnIndentLine.getQuantity());
                }else{
                    Integer currentQuantity = masterProductQuantityMapReturnIndent.get(returnIndentLine.getProduct().getId());
                    masterProductQuantityMapReturnIndent.put(returnIndentLine.getProduct().getId(), currentQuantity+returnIndentLine.getQuantity());
                }
            }
        }

        //Get all the indent lines from parent indent
        Set<IndentLine> parentIndentLines = indentLineRepository.getAllIndentLinesForIndent(indent.getId());
        //Check if for any of the products mentioned, total quantity from all previous returns exceeds original quantity. If fo return false along with error
        System.out.println("\n Master Product Map PArent Indent: "+masterProductQuantityMapParentIndent);
        System.out.println("\n Master Product Map All Return Indents: "+masterProductQuantityMapReturnIndent);
        if(masterProductQuantityMapReturnIndent.size() > 0) {
            for(IndentLineViewModel returnIndentLineViewModel: returnIndentViewModels){
                int parentProductQuantity = masterProductQuantityMapParentIndent.get(returnIndentLineViewModel.getProduct_id());
                int returnIndentTotalProductQuantity = masterProductQuantityMapReturnIndent.get(returnIndentLineViewModel.getProduct_id());
                int currentQuantity = returnIndentLineViewModel.getQuantity();

                if(returnIndentTotalProductQuantity + currentQuantity > parentProductQuantity ){
                    setValidationErrorObject("ValidationError", String.format("You have already returned %d units of Product %d. You can further return %d units only",
                            returnIndentTotalProductQuantity,returnIndentLineViewModel.getProduct_id(),parentProductQuantity - returnIndentTotalProductQuantity ),
                            "ReturnIndent", "IndentLine");
                    return false;
                }
            }
        }
        return true;
    }

    private void setValidationErrorObject(String errorType, String message, String object,  String field){
        validationErrorObj.clear();
        validationErrorObj.put("ErrorType", errorType);
        validationErrorObj.put("Object", object);
        validationErrorObj.put("Field", field);
        validationErrorObj.put("Message", message);
    }

    public ResponseEntity getIndentLines(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }

        Set<IndentLineViewModel> indentLineViewModels = new HashSet<>();
        Set<IndentLine> indentLines = indent.getIndentLineList();
        if(indentLines.size() == 0){
            indentLines = indentLineRepository.getAllIndentLinesForIndent(indent.getId());
        }
        for(IndentLine indentLine: indentLines){
            indentLineViewModels.add(mapToIndentLineViewModel(indentLine));
        }
        return ResponseEntity.status(HttpStatus.OK).body(indentLineViewModels);
    }

    public IndentViewModel mapToIndentViewModel(Indent indent){
        IndentViewModel indentViewModel = new IndentViewModel();
        indentViewModel.setType(indent.getType());
        indentViewModel.setDeliveryDate(indent.getDeliveryDate().toString());
        indentViewModel.setId(indent.getId());
        indentViewModel.setLocation_id(indent.getLocation().getId());
        indentViewModel.setRaisedBy(indent.getRaisedBy().getDisplayName());
        if(indent.getRaisedBy() != null){
            indentViewModel.setRaisedBy(indent.getRaisedBy().getDisplayName());
        }

        indentViewModel.setRemarks(indent.getRemarks());
        indentViewModel.setStatus(indent.getStatus());
        indentViewModel.setTotalPrice(indent.getTotalPrice());
        //Map indent line items
        Set<IndentLineViewModel> indentLineViewModelSet = new HashSet<>();
        Set<IndentLine> indentLines = indent.getIndentLineList();
        //TODO: Getting indentlines from indent is not working automatically. Hence as a temp fix, getting the indent lines explicitly
        if (indentLines.size() == 0){
            indentLines = indentLineRepository.getAllIndentLinesForIndent(indent.getId());
        }

        for(IndentLine indentLine: indentLines){
            IndentLineViewModel indentLineViewModel = mapToIndentLineViewModel(indentLine);
            indentLineViewModelSet.add(indentLineViewModel);
        }
        indentViewModel.setIndentLineList(indentLineViewModelSet);
        return indentViewModel;
    }

    public IndentLineViewModel mapToIndentLineViewModel(IndentLine indentLine){
        IndentLineViewModel indentLineViewModel = new IndentLineViewModel();

        indentLineViewModel.setId(indentLine.getId());
        indentLineViewModel.setProduct_id(indentLine.getProduct().getId());
        indentLineViewModel.setQuantity(indentLine.getQuantity());
        indentLineViewModel.setUnitPrice(indentLine.getUnitPrice());

        return indentLineViewModel;
    }

    public ReturnIndentViewModel mapToReturnIndentView(ReturnIndent returnIndent){
        ReturnIndentViewModel returnIndentViewModel = new ReturnIndentViewModel();
        returnIndentViewModel.setType(returnIndent.getType().toString());
        returnIndentViewModel.setReturnDate(returnIndent.getReturnDate().toString());
        returnIndentViewModel.setId(returnIndent.getId());
        returnIndentViewModel.setRemarks(returnIndent.getRemarks());

        returnIndentViewModel.setStatus(returnIndent.getStatus());

        //Map indent line items
        Set<IndentLineViewModel> indentLineViewModelSet = new HashSet<>();
        Set<IndentLine> indentLines = returnIndent.getIndentLineList();
        //TODO: Getting indentlines from indent is not working automatically. Hence as a temp fix, getting the indent lines explicitly
        if (indentLines.size() == 0){
            indentLines = indentLineRepository.getAllIndentLinesForReturnIndent(returnIndent.getId());
        }

        System.out.println("\n Indent lines for return indent: "+indentLineRepository.getAllIndentLinesForReturnIndent(returnIndent.getId()));
        for(IndentLine indentLine: indentLines){
            IndentLineViewModel indentLineViewModel = mapToIndentLineViewModel(indentLine);
            indentLineViewModelSet.add(indentLineViewModel);
        }
        returnIndentViewModel.setIndentLineList(indentLineViewModelSet);
        return returnIndentViewModel;
    }

    public ResponseEntity getIndentConfigurations() {
        //Return a Hash with all the relevant Static/Config data related to indents, indentlines, return indents
        Map<String, Object> indentConfig = new HashMap<>();

        indentConfig.put("IndentType", IndentType.values());
        indentConfig.put("IndentStatus", IndentStatus.values());
        indentConfig.put("ReturnIndentType", ReturnIndentType.values());
        indentConfig.put("ReturnIndentStatus", ReturnIndentStatus.values());

        return ResponseEntity.status(HttpStatus.OK).body(indentConfig);
    }
}
