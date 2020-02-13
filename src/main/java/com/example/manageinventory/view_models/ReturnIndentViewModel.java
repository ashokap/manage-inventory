package com.example.manageinventory.view_models;

import com.example.manageinventory.models.ReturnIndentStatus;

import java.util.Set;

/**
 * Created by pana on 24/01/20.
 */
public class ReturnIndentViewModel {

    private int id;

    private String type;

    private String remarks;

    private ReturnIndentStatus status;

    Set<IndentLineViewModel> indentLineList;

    private String returnDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<IndentLineViewModel> getIndentLineList() {
        return indentLineList;
    }

    public void setIndentLineList(Set<IndentLineViewModel> indentLineList) {
        this.indentLineList = indentLineList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
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
}
