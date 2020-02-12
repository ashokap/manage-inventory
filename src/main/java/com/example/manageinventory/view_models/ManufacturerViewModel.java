package com.example.manageinventory.view_models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pana on 24/01/20.
 */

public class ManufacturerViewModel {
    private Long id;

    private String name;

    private String address1;

    private String address2;

    private String address3;

    private Boolean available;

    private List<Long> productList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public List<Long> getProductList() {
        return productList;
    }

    public void setProductList(List<Long> productList) {
        this.productList = productList;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
