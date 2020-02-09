package com.example.manageinventory.view_models;

import java.util.List;

/**
 * Created by pana on 24/01/20.
 */
public class IndentViewModel {

    private int id;

    private String type;

    List<IndentLineViewModel> indentLines;

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

    @Override
    public String toString() {
        return "IndentViewModel{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", indentLines=" + indentLines +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", raisedBy=" + raisedBy +
                ", totalPrice=" + totalPrice +
                ", location_id=" + location_id +
                '}';
    }
}
