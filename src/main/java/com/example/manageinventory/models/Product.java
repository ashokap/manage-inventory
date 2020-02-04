package com.example.manageinventory.models;

import javax.persistence.*;

/**
 * Created by pana on 24/01/20.
 */
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "upc_code")
    private String upcCode;
    @Column(name = "hsn_code")
    private String hsnCode;
    @Column(name = "name")
    private String name;
//    @Column(name = "category")
//    private ProductCategory category;
    @Column(name = "sku")
    private String sku;
    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private ProductStatus status;

//    @ManyToOne
//    @JoinColumn(name = "manufacturer_id", nullable = false)
//    private Manufacturer manufacturer;

    private double price;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public ProductCategory getCategory() {
//        return category;
//    }
//
//    public void setCategory(ProductCategory category) {
//        this.category = category;
//    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

//    public Manufacturer getManufacturer() {
//        return manufacturer;
//    }
//
//    public void setManufacturer(Manufacturer manufacturer) {
//        this.manufacturer = manufacturer;
//    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
