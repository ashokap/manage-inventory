package com.example.manageinventory.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pana on 24/01/20.
 */
@Entity(name = "return_indent")
public class ReturnIndent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "type")
    private ReturnIndentType type;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "status")
    private ReturnIndentStatus status;
    @OneToOne
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private Indent indent;

    @OneToMany(mappedBy = "returnIndent", cascade = CascadeType.ALL)
    Set<IndentLine> indentLineList = new HashSet<>();

    @Column(name = "return_date")
    private Date returnDate;

    public ReturnIndent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReturnIndentType getType() {
        return type;
    }

    public void setType(ReturnIndentType type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ReturnIndentStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnIndentStatus status) {
        this.status = status;
    }

    public Indent getIndent() {
        return indent;
    }

    public void setIndent(Indent indent) {
        this.indent = indent;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Set<IndentLine> getIndentLineList() {
        return indentLineList;
    }

    public void setIndentLineList(Set<IndentLine> indentLineList) {
        this.indentLineList = indentLineList;
    }

    public void addIndentLine(IndentLine indentLine){
        System.out.println("Current IndentList: "+indentLineList);
        if(indentLineList == null){
            indentLineList = new HashSet<>();
        }
        indentLineList.add(indentLine);
        indentLine.setReturnIndent(this);
    }


}
