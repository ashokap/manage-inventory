package com.example.manageinventory.view_models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pana on 24/01/20.
 */

public class ProductViewModel {
    private int id;
    private String upcCode;
    private String hsnCode;
    private String name;
    private String category;
    private String sku;
    private String description;
    private String status;
    private List<Integer> locationList = new ArrayList<>();

    private int manufacturer_id;

    private int quantity;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(int manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
    }

    public List<Integer> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Integer> locationList) {
        this.locationList = locationList;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductViewModel{" +
                "id=" + id +
                ", upcCode='" + upcCode + '\'' +
                ", hsnCode='" + hsnCode + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", sku='" + sku + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", manufacturer_id=" + manufacturer_id +
                '}';
    }
}
