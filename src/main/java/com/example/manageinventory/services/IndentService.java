package com.example.manageinventory.services;

import com.example.manageinventory.models.*;
import com.example.manageinventory.repositories.IndentRepository;
import com.example.manageinventory.repositories.ProductRepository;
import com.example.manageinventory.repositories.ReturnIndentRepository;
import com.example.manageinventory.view_models.IndentLineViewModel;
import com.example.manageinventory.view_models.IndentViewModel;
import com.example.manageinventory.view_models.LocationViewModel;
import com.example.manageinventory.view_models.ReturnIndentViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IndentService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
    
    private IndentRepository indentRepository;
    private ReturnIndentRepository returnIndentRepository;
    private ProductRepository productRepository;

    public ResponseEntity getListOfIndents(){
        return ResponseEntity.status(HttpStatus.OK).body(this.indentRepository.findAll());
    }

    public ResponseEntity getIndentById(int id) {
        Indent indent = this.indentRepository.findIndentById(id);

        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("indent with ID: %d not found",id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(indent);
    }

    public ResponseEntity createNewIndent(IndentViewModel indentViewModel) {

        Indent indent = new Indent();

        BeanUtils.copyProperties(indentViewModel, indent, "id","status");
        if(indent.getType().toString().equalsIgnoreCase(IndentType.INCOMING.toString())){
            indent.setStatus(IndentStatus.ORDER_RECEIVED);
        }else{
            indent.setStatus(IndentStatus.ORDER_PLACED);
        }

        indentRepository.saveAndFlush(indent);
        return ResponseEntity.status(HttpStatus.CREATED).body(indent);
    }

    public ResponseEntity updateIndentDetails(int id, IndentViewModel indentViewModel) {

        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }

        BeanUtils.copyProperties(indentViewModel, indent, "id","status");
        indentRepository.saveAndFlush(indent);
        return ResponseEntity.status(HttpStatus.CREATED).body(indent);
    }

    public ResponseEntity markIndentAsDeleted(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }
        indent.setStatus(IndentStatus.DELETED);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Indent %d has been marked as Deleted", indent.getId()));
    }

    public ResponseEntity getLocationsForIndent(int id) {
        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(indent.getLocation());
    }

    @Transactional
    public ResponseEntity createReturnForIndent(int id, ReturnIndentViewModel indentViewModel) {

        Indent indent = this.indentRepository.findIndentById(id);
        if(indent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Indent with ID: %d not found",id));
        }
        ReturnIndent returnIndent = new ReturnIndent();
        //If indent type is incoming, then create return of type incoming
        if(indent.getType() == IndentType.INCOMING){
            returnIndent.setType(ReturnIndentType.RETURN_INCOMING);
        }else{
            returnIndent.setType(ReturnIndentType.RETURN_OUTGOING);
        }

        //Check the product list and quantities and make sure no other products or quantity exceeding the original indent are being returned
        if(indentViewModel.getIndentLines().size() <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Return Indent should atleast contain 1 product"));
        }else{
            List<IndentLine> indentLineList = indent.getIndentLineList();

            //Go through each incoming indentline and cross check with existing indent's indentlines and
            // return error if any of the product gets mismatched
            for(IndentLineViewModel indentLineViewModel: indentViewModel.getIndentLines()){
                IndentLine indentLineFound = null;
                //Go through each of the indent's indentlines and see if the product is present or not
                for(IndentLine indentLine: indentLineList){
                    if(indentLine.getProduct().getId() == indentLineViewModel.getProduct_id()){
                        indentLineFound = indentLine;
                        break;
                    }
                }
                //If no such indent line found, return error
                if (indentLineFound == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product ID %d not found within the original indent %d ",indentLineViewModel.getProduct_id(), indent.getId()));
                }
                //If relevant product found, make sure that the quantity is <= original quantity
                if(indentLineFound.getQuantity() < indentLineViewModel.getQuantity()){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("You can only return %d items for the product %d ",indentLineFound.getQuantity(), indentLineFound.getProduct().getId() ));
                }
            }

            //All fine, now create the return indent, also credit/debit products in the inentory

            returnIndent.setIndent(indent);

            BeanUtils.copyProperties(indentViewModel, returnIndent, "id","status","indentLineList");
            if(indent.getType().toString().equalsIgnoreCase(IndentType.INCOMING.toString())){
                returnIndent.setStatus(IndentStatus.ORDER_RECEIVED);
            }else{
                returnIndent.setStatus(IndentStatus.ORDER_PLACED);
            }
            //Add all indentlines
            for(IndentLineViewModel indentLineViewModel: indentViewModel.getIndentLines()){
                IndentLine indentLine = new IndentLine();
                Product product = productRepository.findProductById(indentLineViewModel.getProduct_id());
                indentLine.setIndent(indent);
                indentLine.setProduct(product);
                indentLine.setQuantity(indentLineViewModel.getQuantity());
                indentLine.setUnitPrice(indentLineViewModel.getUnitPrice());
                returnIndent.getIndentLineList().add(indentLine);

                //Now update product's quantity based on return type
                int existingQuantity = product.getQuantity();
                if(returnIndent.getType().toString().equalsIgnoreCase(ReturnIndentType.RETURN_INCOMING.toString())){
                    product.setQuantity(existingQuantity - indentLineViewModel.getQuantity());
                }else{
                    product.setQuantity(existingQuantity + indentLineViewModel.getQuantity());
                }
                this.productRepository.saveAndFlush(product);
            }

            //Save the return indent
            returnIndentRepository.saveAndFlush(returnIndent);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnIndent);
        }


    }
}
