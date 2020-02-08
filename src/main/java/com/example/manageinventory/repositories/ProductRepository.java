package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Location;
import com.example.manageinventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findProductById(Integer id);
    Product findProductBySku(String sku);
//
//    @Query("SELECT p FROM product p JOIN FETCH p.locations WHERE p.id = :id")
//    Set<Location> findLocationsByProductId(@Param("id") int id);

}
