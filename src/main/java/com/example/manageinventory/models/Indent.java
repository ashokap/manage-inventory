package com.example.manageinventory.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by pana on 24/01/20.
 */
@Entity(name = "indent")
public class Indent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "type")
    private IndentType type;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "status")
    private IndentStatus status;

    @OneToMany(mappedBy = "indent")
    List<IndentLine> indentLineList;
    @Column(name = "delivery_date")
    private Date deliveryDate;
    @OneToOne
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JoinColumn(name = "user_id")
    private User raisedBy;
    @Column(name = "total_price")
    private double totalPrice;

    @OneToOne
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JoinColumn(name = "location_id")
    private Location location;

    public Indent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IndentType getType() {
        return type;
    }

    public void setType(IndentType type) {
        this.type = type;
    }

    public List<IndentLine> getIndentLineList() {
        return indentLineList;
    }

    public void setIndentLineList(List<IndentLine> indentLineList) {
        this.indentLineList = indentLineList;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public User getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(User raisedBy) {
        this.raisedBy = raisedBy;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public IndentStatus getStatus() {
        return status;
    }

    public void setStatus(IndentStatus status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
