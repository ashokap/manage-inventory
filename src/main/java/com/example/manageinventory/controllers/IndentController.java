package com.example.manageinventory.controllers;

import com.example.manageinventory.constants.APIConstants;
import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.IndentType;
import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.repositories.IndentRepository;
import com.example.manageinventory.repositories.ManufacturerRepository;
import com.example.manageinventory.repositories.ProductRepository;
import com.example.manageinventory.services.IndentService;
import com.example.manageinventory.view_models.IndentViewModel;
import com.example.manageinventory.view_models.LocationViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.security.acl.AllPermissionsImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = APIConstants.Indent.INDENT_ROOT)
public class IndentController {

    @Autowired
    private IndentService indentService;

    //Get list of products in the inventory
    @GetMapping
    public ResponseEntity getListOfIndents(){
        try {
            return this.indentService.getListOfIndents();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    //Get details of an inventory
    @GetMapping(path = APIConstants.Indent.INDENT_GET_UPDATE_DELETE)
    public ResponseEntity getLocationById(
            @PathVariable int id) {
        try {
            return this.indentService.getIndentById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity createIndent(@RequestBody final IndentViewModel indentViewModel) {
        try {
            return this.indentService.createNewIndent(indentViewModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PutMapping(path = APIConstants.Indent.INDENT_GET_UPDATE_DELETE)
    public ResponseEntity updateLocation(@RequestBody final IndentViewModel indentViewModel,
                                         @PathVariable int id) {
        try{
            return this.indentService.updateIndentDetails(id, indentViewModel);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }


    @GetMapping(path = APIConstants.Indent.INDENT_LOCATIONS)
    public ResponseEntity getLocationsForIndent(
            @PathVariable int id) {
        return null;
    }

    @GetMapping(path = APIConstants.Indent.INDENT_RETURN_CREATE)
    public ResponseEntity createReturnForIndent(@PathVariable int id, @RequestBody IndentViewModel indentViewModel){
        return null;
    }






}
