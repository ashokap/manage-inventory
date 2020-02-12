package com.example.manageinventory.services;

import com.example.manageinventory.constants.InventoryConstants;
import com.example.manageinventory.models.*;
import com.example.manageinventory.repositories.*;
import com.example.manageinventory.view_models.IndentLineViewModel;
import com.example.manageinventory.view_models.IndentViewModel;
import com.example.manageinventory.view_models.ReturnIndentViewModel;
import org.springframework.beans.BeanUtils;
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

    public static Map<String, String> validationErrorObj =  new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }

    public ResponseEntity getListOfIndents(){
        return ResponseEntity.status(HttpStatus.OK).body(this.indentRepository.findAll());
    }

    public ResponseEntity getIndentById(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent Not Found for ID: %d", id));
        }

        System.out.println(indent.getIndentLineList());
        return ResponseEntity.status(HttpStatus.OK).body(indent);
    }

    @Transactional
    public ResponseEntity createNewIndent(IndentViewModel indentViewModel) throws ParseException {

        Indent indent = new Indent();

        Date deliveryDate= new SimpleDateFormat(InventoryConstants.DefaultDateFormat).parse(indentViewModel.getDeliveryDate());
        indent.setDeliveryDate(deliveryDate);
        indent.setType(indentViewModel.getType());
        indent.setRemarks(indentViewModel.getRemarks());
        //Set status based on type
        if(indent.getType() == IndentType.INCOMING){
            indent.setStatus(IndentStatus.ORDER_PLACED);
        }else{
            indent.setStatus(IndentStatus.ORDER_RECEIVED);
        }

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
        System.out.println(String.format("\n Indent Obj: %s, Indent Model: %s \n",indent.toString(), indentViewModel.toString() ));
        indentRepository.saveAndFlush(indent);
        return ResponseEntity.status(HttpStatus.CREATED).body(indent);
    }

    private Boolean validateIndentViewLines(List<IndentLineViewModel> indentLineViewModels, Indent indent){
        if(indentLineViewModels.size() <= 0){
            setValidationErrorObject("Validation", "Atleast one product must be present while creating Indent",
                    "Indent", "IndentLines");
            return false;
        }

        //Go through each incoming indentline and cross check if the product exists or not. If not return error
        // return error if any of the product gets mismatched
//        List<IndentLine> indentLines = new ArrayList<>();

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

        System.out.println(String.format("\n Indent's IndentLines: %d", indent.getIndentLineList().size()));

        return true;
    }

    private boolean validateAndSetLocation(int location_id, Indent indent, Boolean updateProductQuantity) {
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
            //TODO: Need to populate indent_id and quantity to this "location_product" table
        }
        if(updateProductQuantity) {
            indent.setTotalPrice(totalPrice);
        }
        indent.setLocation(location);
        return true;
    }

    @Transactional
    public ResponseEntity updateIndentDetails(int id, IndentViewModel indentViewModel) throws ParseException {

        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }

        List<String> indentStatusArray = Arrays.asList(IndentStatus.DELETED.toString(), IndentStatus.DISPATCHED.toString(), IndentStatus.PROCESSED.toString());

        if(indentStatusArray.contains(indent.getStatus().toString())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Indent has been marked as %s and hence cannot be updated further", indent.getStatus().toString()));
        }
        //Incoming Indent's status can only be updated to "Processed" and outgoing indent's status can only be updated to "Dispatched
        if((indentViewModel.getStatus()!= null) &&
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
        return ResponseEntity.status(HttpStatus.CREATED).body(indent);
    }

    @Transactional
    public ResponseEntity markIndentAsDeleted(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }
        indent.setStatus(IndentStatus.DELETED);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Indent %d has been marked as Deleted", indent.getId()));
    }

    @Transactional
    public ResponseEntity getLocationsForIndent(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(indent.getLocation());
    }

    @Transactional
    public ResponseEntity createReturnForIndent(int id, ReturnIndentViewModel returnIndentViewModel) {

        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Parent Indent with ID: %d not found",id));
        }
        ReturnIndent returnIndent = new ReturnIndent();
        //If indent type is incoming, then create return of type incoming
        if(indent.getType() == IndentType.INCOMING){
            returnIndent.setType(ReturnIndentType.RETURN_INCOMING);
            returnIndent.setStatus(ReturnIndentStatus.INCOMING_RETURN_CREATED);
        }else{
            returnIndent.setType(ReturnIndentType.RETURN_OUTGOING);
            returnIndent.setStatus(ReturnIndentStatus.OUTGOING_RETURN_CREATED);
        }

        //Check the product list and quantities and make sure no other products or quantity exceeding the original indent are being returned
        if(returnIndentViewModel.getIndentLineList() == null || returnIndentViewModel.getIndentLineList().size() <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Return Indent Line should atleast contain 1 product from the original indent."));
        }else{
            //List<IndentLine> indentLineList = indent.getIndentLineList();
            System.out.println(indent.getIndentLineList());
            System.out.println("\n HERE \n");
            List<IndentLine> returnIndentLines = new ArrayList<>();
            //Go through each incoming indentline and cross check with existing indent's indentlines and
            // return error if any of the product gets mismatched
            for(IndentLineViewModel returnIndentLineViewModel: returnIndentViewModel.getIndentLineList()){
                IndentLine indentLineFound = new IndentLine();
                //Go through each of the indent's indentlines and see if the product is present or not
                for(IndentLine parentIndentLine: indent.getIndentLineList()){
                    if(parentIndentLine.getProduct().getId() == returnIndentLineViewModel.getProduct_id()){
                        indentLineFound = parentIndentLine;
                        break;
                    }
                }
                //If no such indent line found, return error
                if (indentLineFound.getProduct() == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product ID %d not found within the original indent %d ",returnIndentLineViewModel.getProduct_id(), indent.getId()));
                }
                //If relevant product found, make sure that the quantity is <= original quantity
                if(indentLineFound.getQuantity() < returnIndentLineViewModel.getQuantity()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("You can only return %d items for the product %d ",indentLineFound.getQuantity(), indentLineFound.getProduct().getId() ));
                }
                //If relevant product found, make sure that the price is unchanged
                if(indentLineFound.getUnitPrice() != returnIndentLineViewModel.getUnitPrice()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Price mismatch while returning product %d. Original price: %d, New Price: %d ",indentLineFound.getProduct().getId(), indentLineFound.getUnitPrice(), returnIndentLineViewModel.getUnitPrice() ));
                }
                //All fine. Assign the quantity and price from return indent view model and assign the line to return indent
                indentLineFound.setQuantity(returnIndentLineViewModel.getQuantity());
                indentLineFound.setUnitPrice(returnIndentLineViewModel.getUnitPrice());
                indentLineFound.setReturnIndent(returnIndent);
                returnIndentLines.add(indentLineFound);
            }

            //All fine, now create the return indent, also credit/debit products in the inventory
            returnIndent.setIndent(indent);
            //Add all the return indent lines prepared above
            returnIndent.setIndentLineList(returnIndentLines);
            returnIndent.setReturnDate(new Date());
            returnIndent.setRemarks(returnIndentViewModel.getRemarks());

            //Now update product's quantity based on the retuned line items
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

            //Save the return indent
            returnIndentRepository.saveAndFlush(returnIndent);
            //Save return indent line items
            indentLineRepository.saveAll(returnIndentLines);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnIndent);
        }

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

        return ResponseEntity.status(HttpStatus.OK).body(indent.getIndentLineList());
    }
}
