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
@CrossOrigin
@RequestMapping(path = APIConstants.Product.PRODUCT_ROOT)
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * //Get list of products in the inventory
     * @return
     */
    @GetMapping
    public ResponseEntity listOfProducts() {
        try {


            return this.productService.getListOfProducts();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * View Product details
     * @param id
     * @return
     */

    //Get details of a product
    @GetMapping(path = APIConstants.Product.PRODUCT_GET_UPDATE_DELETE)
    public ResponseEntity getProductById(
            @PathVariable int id) {
        try {
            return this.productService.getProductById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Create/register a new Product. Manufacturer should be created before hand
     * @param product
     * @return
     */

    @PostMapping
    public ResponseEntity create(@RequestBody final ProductViewModel product) {
        try {
            return this.productService.registerNewProduct(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    /**
     * Update Product Details
     * @param product
     * @param id
     * @return
     */

    @PutMapping(path = APIConstants.Product.PRODUCT_GET_UPDATE_DELETE)
    public ResponseEntity updateProduct(@RequestBody final ProductViewModel product,
                                        @PathVariable int id) {
        try{
            return this.productService.updateProductDetails(id, product);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    /**
     * API to soft delete Product.
     * @param id
     * @return
     */
    @DeleteMapping(path = APIConstants.Product.PRODUCT_GET_UPDATE_DELETE)
    public ResponseEntity deleteProduct(@PathVariable int id) {
        try{
            return this.productService.updateProductMarkAsDelete(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get all the locations for a given product
     * @param id
     * @return
     */
    //Get details of a product
    @GetMapping(path = APIConstants.Product.PRODUCT_LOCATIONS)
    public ResponseEntity getLocationsForProduct(
            @PathVariable int id) {
        try {
            return this.productService.getLocationsForProduct(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * //Get all products who's quanity <= a predefined low stock
     * @return
     */
    @GetMapping(path = APIConstants.Product.PRODUCT_CRITICAL_STOCK)
    public ResponseEntity getCriticallyLowProducts() {
        try {
            return this.productService.getListOfProductsWithLowStock();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * //Get all products who's quanity <= a user defined quantity
     * @return
     */
    @GetMapping(path = APIConstants.Product.PRODUCT_SPECIFIC_STOCK)
    public ResponseEntity getProductsWithSpecificStock(@RequestParam int quantity) {
        try {
            return this.productService.getListOfProductsWithSpecificStock(quantity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * TODO
     * @param fromDate
     * @param toDate
     * @return
     */

    @GetMapping(path = APIConstants.Product.PRODUCT_FAST_MOVING)
    public ResponseEntity getFastMovingProducts(
            @RequestParam String fromDate, @RequestParam String toDate) {
        try {
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * TODO
     * @param fromDate
     * @param toDate
     * @return
     */

    @GetMapping(path = APIConstants.Product.PRODUCT_SLOW_MOVING)
    public ResponseEntity getSlowMovingProducts(
            @RequestParam String fromDate, @RequestParam String toDate) {
        try {
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get all the static data required to manage product
     * @return
     */
    @GetMapping(path = APIConstants.Product.PRODUCT_CONFIG)
    public ResponseEntity getProductConfigs(){
        try {
            return this.productService.getProductConfigurations();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
