package com.example.manageinventory.services;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.IndentStatus;
import com.example.manageinventory.models.IndentType;
import com.example.manageinventory.models.Location;
import com.example.manageinventory.repositories.IndentRepository;
import com.example.manageinventory.view_models.IndentViewModel;
import com.example.manageinventory.view_models.LocationViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IndentService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
    
    private IndentRepository indentRepository;

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
    
}
