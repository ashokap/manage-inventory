package com.example.manageinventory.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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

    @OneToMany(mappedBy = "indent")
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    Set<IndentLine> indentLineList;

    private Date indentDate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User raisedBy;
    @Column(name = "total_price")
    private double totalPrice;
    @OneToOne
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

    public Set<IndentLine> getIndentLineList() {
        return indentLineList;
    }

    public void setIndentLineList(Set<IndentLine> indentLineList) {
        this.indentLineList = indentLineList;
    }

    public Date getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(Date indentDate) {
        this.indentDate = indentDate;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
