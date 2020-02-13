package com.example.manageinventory.repositories;

import com.example.manageinventory.models.IndentLine;
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

    @Query("SELECT p from product p WHERE p.quantity <= ?1")
    List<Product> getLowStockProducts(int quantity);


}
