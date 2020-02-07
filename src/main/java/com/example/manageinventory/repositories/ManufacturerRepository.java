package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    Manufacturer findManufacturerById(Integer id);

}
