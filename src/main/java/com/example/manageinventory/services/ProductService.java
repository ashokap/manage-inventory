package com.example.manageinventory.services;

import com.example.manageinventory.models.Location;
import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.models.ProductStatus;
import com.example.manageinventory.repositories.ManufacturerRepository;
import com.example.manageinventory.repositories.ProductRepository;
import com.example.manageinventory.view_models.ProductViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService implements InitializingBean {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * Product List API
     * @return
     */
    public ResponseEntity getListOfProducts(){

        return ResponseEntity.status(HttpStatus.OK).body(this.productRepository.findAll());
    }

    /**
     * Get details of a product
     * @param id
     * @return
     */
    public ResponseEntity getProductById(int id) {

        Product existingProduct = this.productRepository.findProductById(id);
        if (existingProduct == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with ID: %d not found",id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(existingProduct);
    }

    /**
     * Accept a Product View model and register a new Product. Product should have a unique SKU and should be from an existing Manufacturer
     * @param product
     * @return
     */
    public ResponseEntity registerNewProduct(ProductViewModel product){
        //Optional<Manufacturer> parentManufacturer = manufacturerRepository.findById(product.getManufacturer_id());
        Manufacturer parentManufacturer = this.manufacturerRepository.findManufacturerById(product.getManufacturer_id());
        if (parentManufacturer != null){
            Product productObj = new Product();
            productObj.setManufacturer(parentManufacturer);
            BeanUtils.copyProperties(product, productObj, "manufacturer_id");
            productObj.setStatus(ProductStatus.REGISTERED);
            productRepository.saveAndFlush(productObj);
            return ResponseEntity.status(HttpStatus.CREATED).body(productObj);

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Manufacturer with ID: %d not found. Cannot create product", product.getManufacturer_id()));
        }
    }

    /**
     * Update Service. Check if manufacturer has been changed and assign appropriately. Location cannot be assigned
     * @param id
     * @param product
     * @return
     */
    public ResponseEntity updateProductDetails(int id, ProductViewModel product) {
        //First get the product
        Product existingProduct = productRepository.findProductById(id);

        if (existingProduct!= null){
            Manufacturer newManufacturer = null;
            //Now check if the manufacturer is being updated. If so check if the new manufacturer exists
            if(existingProduct.getManufacturer().getId() != product.getManufacturer_id()){
                newManufacturer = this.manufacturerRepository.findManufacturerById(product.getManufacturer_id());
                if(newManufacturer == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Manufacturer with ID: %d not found. Cannot update product %d", product.getManufacturer_id(), id));
                }
                //Assign new manufacturer
                existingProduct.setManufacturer(newManufacturer);
            }

            //Now copy all the relevant data to Product DB
            BeanUtils.copyProperties(product, existingProduct, "manufacturer_id","id");
            productRepository.saveAndFlush(existingProduct);
            return ResponseEntity.status(HttpStatus.OK).body(existingProduct);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with ID: %d not found",id));
        }
    }

    /**
     * Marking Product for Soft Deletion. These products should not appear in any other reports or indents
     * @param id
     * @return
     */
    public ResponseEntity updateProductMarkAsDelete(int id) {
        //First get the product
        Product existingProduct = productRepository.findProductById(id);

        if (existingProduct!= null){
            //Mark the product as Deleted
            existingProduct.setStatus(ProductStatus.DELETED);
            productRepository.saveAndFlush(existingProduct);
            return ResponseEntity.status(HttpStatus.OK).body(String.format("Product %s has been marked as DELETED", existingProduct.getSku()));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product %d not found", id));
        }
    }

    public ResponseEntity getLocationsForProduct(int id) {
        //First get the product
        Product existingProduct = productRepository.findProductById(id);

        if (existingProduct!= null){
            //Get the locations from locations_products table
            System.out.println("Inside Product Location Fetch: Before"+existingProduct.getLocations());
//            Set<Location> locations = this.productRepository.findLocationsByProductId(id);
//            System.out.println("Inside Product Location Fetch: After \n");
//            System.out.println(locations);
            return ResponseEntity.status(HttpStatus.OK).body(existingProduct.getLocations());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product %d not found", id));
        }

    }
}
