package com.example.manageinventory.controllers;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.IndentType;
import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.repositories.IndentRepository;
import com.example.manageinventory.repositories.ManufacturerRepository;
import com.example.manageinventory.repositories.ProductRepository;
import com.example.manageinventory.view_models.IndentViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/1/ims/indents")
public class IndentController {

    @Autowired
    private IndentRepository indentRepository;

    //Get list of products in the inventory
    @GetMapping("list")
    public List<Indent> list() {
        return this.indentRepository.findAll();
    }

    //Get details of an inventory
    @GetMapping("{id}")
    public Optional<Indent> getById(
            @PathVariable int id) {
        return this.indentRepository.findById(id);
    }

    @PostMapping
    public IndentViewModel create(@RequestBody final IndentViewModel indentViewModel) {
        //Based on the indent type, create new indent records and process products
        switch (indentViewModel.getType()){
            case "INCOMING":
                //Register products if not done already
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + indentViewModel.getType());
        }
        return new IndentViewModel();


    }



}
