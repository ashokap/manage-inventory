package com.example.manageinventory.controllers;

import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.repositories.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/1//ims/manufacturers")
public class ManufacturerController {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    //Get list of products in the inventory
    @GetMapping("list")
    public List<Manufacturer> list() {
        return this.manufacturerRepository.findAll();
    }

    //Get details of a product
    @GetMapping("{id}")
    public Optional<Manufacturer> getById(
            @PathVariable int id) {
        return this.manufacturerRepository.findById(id);
    }

    @PostMapping
    public Manufacturer create(@RequestBody final Manufacturer manufacturer) {
        return manufacturerRepository.saveAndFlush(manufacturer);
    }




}
