package com.example.manageinventory.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * Created by pana on 24/01/20.
 */
@Entity(name = "indent_line")
public class IndentLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne//This is a uni directional one-to-one mapping where each indent line holds 1 product. Bi directional is not needed
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "unit_price")
    private double unitPrice;

    @ManyToOne
    @JoinColumn(name = "indent_id", nullable = false)
    private Indent indent;

    @ManyToOne
    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JoinColumn(name = "return_indent_id", nullable = false)
    private ReturnIndent returnIndent;

    public IndentLine() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Indent getIndent() {
        return indent;
    }

    public void setIndent(Indent indent) {
        this.indent = indent;
    }

    public ReturnIndent getReturnIndent() {
        return returnIndent;
    }

    public void setReturnIndent(ReturnIndent returnIndent) {
        this.returnIndent = returnIndent;
    }
}
