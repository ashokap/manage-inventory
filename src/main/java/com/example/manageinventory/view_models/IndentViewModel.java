package com.example.manageinventory.view_models;

import com.example.manageinventory.models.IndentType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by pana on 24/01/20.
 */
public class IndentViewModel {

    private Long id;

    private String type;

    List<IndentLineViewModel> indentLines;

    private Date indentDate;

    private Long raisedBy;

    private double totalPrice;

    private Long location_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IndentLineViewModel> getIndentLines() {
        return indentLines;
    }

    public void setIndentLines(List<IndentLineViewModel> indentLines) {
        this.indentLines = indentLines;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(Date indentDate) {
        this.indentDate = indentDate;
    }

    public Long getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(Long raisedBy) {
        this.raisedBy = raisedBy;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }
}
