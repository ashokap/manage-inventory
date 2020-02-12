package com.example.manageinventory.controllers;

import com.example.manageinventory.constants.APIConstants;
import com.example.manageinventory.services.ManufacturerService;
import com.example.manageinventory.view_models.ManufacturerViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = APIConstants.Manufacturer.MANUFACTURER_ROOT)
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    //Get list of products in the inventory
    @GetMapping

    public ResponseEntity listOfManufacturers() {
        try {
            return this.manufacturerService.getListOfManufacturers();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Get details of a product
    @GetMapping("{id}")
    public ResponseEntity getById(
            @PathVariable int id) {
        try {
            return this.manufacturerService.getManufacturerById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(path = APIConstants.Manufacturer.MANUFACTURER_GET_UPDATE_DELETE)
    public ResponseEntity deleteManufacturer(@PathVariable int id) {
        try{
            return this.manufacturerService.updateManufacturerMarkAsUnavailable(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody final ManufacturerViewModel manufacturer) {
        try {
            return this.manufacturerService.createNewManufacturer(manufacturer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(path = APIConstants.Manufacturer.MANUFACTURER_GET_UPDATE_DELETE)
    public ResponseEntity update(@PathVariable int id,
                               @RequestBody final ManufacturerViewModel manufacturer) {
        try {
            return this.manufacturerService.updateManufacturerDetails(id, manufacturer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




}
