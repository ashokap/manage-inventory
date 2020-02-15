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
import com.example.manageinventory.view_models.ReturnIndentViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.security.acl.AllPermissionsImpl;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = APIConstants.Indent.INDENT_ROOT)
public class IndentController {

    @Autowired
    private IndentService indentService;



    /**
     * //Get list of indents
     * @return
     */
    @GetMapping
    public ResponseEntity getListOfIndents(){
        try {
            return this.indentService.getListOfIndents();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    /**
     * //Get details of an indent
     * @param id
     * @return
     */
    @GetMapping(path = APIConstants.Indent.INDENT_GET_UPDATE_DELETE)
    public ResponseEntity getIndentById(
            @PathVariable int id) {
        try {
            return this.indentService.getIndentById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get list of all indent lines for a given indent
     * @param id
     * @return
     */
    @GetMapping(path = APIConstants.Indent.INDENT_GET_UPDATE_DELETE+"/indent_lines")
    public ResponseEntity getIndentLines(
            @PathVariable int id) {
        try {
            return this.indentService.getIndentLines(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    /**
     * Create a new Incoming/Outgoing indent
     * @param indentViewModel
     * @return
     */

    @PostMapping
    public ResponseEntity createIndent(@RequestBody final IndentViewModel indentViewModel) {
        try {
            return this.indentService.createNewIndent(indentViewModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    /**
     * Update location, status, remarks for an indent
     * @param indentViewModel
     * @param id
     * @return
     */

    @PutMapping(path = APIConstants.Indent.INDENT_GET_UPDATE_DELETE)
    public ResponseEntity updateIndent(@RequestBody final IndentViewModel indentViewModel,
                                         @PathVariable int id) {
        try{
            return this.indentService.updateIndentDetails(id, indentViewModel);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    /**
     * Get locations for a given indent
     * @param id
     * @return
     */
    @GetMapping(path = APIConstants.Indent.INDENT_LOCATIONS)
    public ResponseEntity getLocationsForIndent(
            @PathVariable int id) {
        try{
            return this.indentService.getLocationsForIndent(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Create a return indent for an incoming/outgoing indent
     * @param id
     * @param returnIndentViewModel
     * @return
     */
    @PostMapping(path = APIConstants.Indent.INDENT_RETURN_CREATE)
    public ResponseEntity createReturnForIndent(@PathVariable int id, @RequestBody ReturnIndentViewModel returnIndentViewModel){
        try{
            return this.indentService.createReturnForIndent(id, returnIndentViewModel);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get list of all returns for the given indent
     * @param id
     * @return
     */
    @GetMapping(path = APIConstants.Indent.INDENT_RETURN_CREATE)
    public ResponseEntity getReturnIndentsForIndent(@PathVariable int id){
        try{
            return this.indentService.getReturnIndentsForIndent(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get configurations for managing indent, return indent
     * @return
     */
    @GetMapping(path = APIConstants.Indent.INDENT_CONFIG)
    public ResponseEntity getIndentConfigs(){
        try {
            return this.indentService.getIndentConfigurations();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
