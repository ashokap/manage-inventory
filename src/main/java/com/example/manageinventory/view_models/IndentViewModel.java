package com.example.manageinventory.view_models;

import com.example.manageinventory.models.IndentStatus;
import com.example.manageinventory.models.IndentType;

import java.util.List;

/**
 * Created by pana on 24/01/20.
 */
public class IndentViewModel {

    private int id;

    private IndentType type;

    private String remarks;

    private IndentStatus status;

    List<IndentLineViewModel> indentLineList;

    private String deliveryDate;

    private int raisedBy;

    private double totalPrice;

    private int location_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<IndentLineViewModel> getIndentLineList() {
        return indentLineList;
    }

    public void setIndentLineList(List<IndentLineViewModel> indentLineList) {
        this.indentLineList = indentLineList;
    }

    public IndentType getType() {
        return type;
    }

    public void setType(IndentType type) {
        this.type = type;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(int raisedBy) {
        this.raisedBy = raisedBy;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public IndentStatus getStatus() {
        return status;
    }

    public void setStatus(IndentStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "IndentViewModel{" +
                "id=" + id +
                ", type=" + type +
                ", remarks='" + remarks + '\'' +
                ", status=" + status +
                ", indentLineList=" + indentLineList +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", raisedBy=" + raisedBy +
                ", totalPrice=" + totalPrice +
                ", location_id=" + location_id +
                '}';
    }
}
