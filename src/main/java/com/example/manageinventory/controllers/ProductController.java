package com.example.manageinventory.controllers;

import com.example.manageinventory.constants.APIConstants;
import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.repositories.ManufacturerRepository;
import com.example.manageinventory.repositories.ProductRepository;
import com.example.manageinventory.services.ProductService;
import com.example.manageinventory.view_models.ProductViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = APIConstants.Product.PRODUCT_ROOT)
public class ProductController {

    @Autowired
    private ProductService productService;

    //Get list of products in the inventory
    @GetMapping(path = APIConstants.Product.PRODUCT_LIST)
    public ResponseEntity list() {
        try {
            return this.productService.getListOfProducts();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //Get details of a product
    @GetMapping(path = APIConstants.Product.PRODUCT_GET)
    public ResponseEntity getBySku(
            @PathVariable String sku) {
        try {
            return this.productService.getProductBySku(sku);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(path = APIConstants.Product.PRODUCT_REGISTER)
    public ResponseEntity create(@RequestBody final ProductViewModel product) {
        try {
            return this.productService.registerNewProduct(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping(path = APIConstants.Product.PRODUCT_UPDATE)
    public ResponseEntity updateProduct(@RequestBody final ProductViewModel product,
                                        @PathVariable String sku) {
        try{
            return this.productService.updateProductDetails(sku, product);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    @PostMapping(path = APIConstants.Product.PRODUCT_DELETE)
    public ResponseEntity deleteProduct(@RequestBody final ProductViewModel product) {
        return null;

    }



}
