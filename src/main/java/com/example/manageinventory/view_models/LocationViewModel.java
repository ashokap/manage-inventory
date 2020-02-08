package com.example.manageinventory.view_models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pana on 24/01/20.
 */
public class LocationViewModel {
    private int id;

    private String description;

    private String type;

    private Boolean isAvailable;

    private List<Integer> productList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public List<Integer> getProductList() {
        return productList;
    }

    public void setProductList(List<Integer> productList) {
        this.productList = productList;
    }
}
