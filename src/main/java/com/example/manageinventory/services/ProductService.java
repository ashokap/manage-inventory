package com.example.manageinventory.services;

import com.example.manageinventory.models.Manufacturer;
import com.example.manageinventory.models.Product;
import com.example.manageinventory.repositories.ManufacturerRepository;
import com.example.manageinventory.repositories.ProductRepository;
import com.example.manageinventory.view_models.ProductViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * Get details of a product using its SKU(Unique identifier)
     * @param sku
     * @return
     */
    public ResponseEntity getProductBySku(String sku) {
        return ResponseEntity.status(HttpStatus.OK).body(this.productRepository.findProductBySku(sku));
    }

    /**
     * Accept a Product View model and register a new Product. Product should have a unique SKU and should be from an existing Manufacturer
     * @param product
     * @return
     */
    public ResponseEntity registerNewProduct(ProductViewModel product){
        Optional<Manufacturer> parentManufacturer = manufacturerRepository.findById(product.getManufacturer_id());
        if (parentManufacturer.isPresent()){
            Product productObj = new Product();
            productObj.setManufacturer(parentManufacturer.get());
            BeanUtils.copyProperties(product, productObj, "manufacturer_id");
            productRepository.saveAndFlush(productObj);
            return ResponseEntity.status(HttpStatus.CREATED).body(productObj);

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Manufacturer with ID: %d not found. Cannot create product", product.getManufacturer_id()));
        }
    }

    public ResponseEntity updateProductDetails(String sku, ProductViewModel product) {
        //First get the product from SKU
        Product existingProduct = productRepository.findProductBySku(sku);

        if (existingProduct!= null){
            Manufacturer newManufacturer = null;
            //Now check if the manufacturer is being updated. If so check if the new manufacturer exists
            if(existingProduct.getManufacturer().getId() != product.getManufacturer_id()){
                newManufacturer = this.manufacturerRepository.findManufacturerById(product.getManufacturer_id());
                if(newManufacturer == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Manufacturer with ID: %d not found. Cannot update product %s", product.getManufacturer_id(), sku));
                }
                //Assign new manufacturer
                existingProduct.setManufacturer(newManufacturer);
            }

            //Now copy all the relevant data to Product DB
            BeanUtils.copyProperties(product, existingProduct, "manufacturer_id");


        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with SKU: %s not found", sku));
        }
        return null;
    }
}
