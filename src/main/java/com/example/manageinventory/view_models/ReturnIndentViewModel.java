package com.example.manageinventory.view_models;

import com.example.manageinventory.models.IndentStatus;
import com.example.manageinventory.models.ReturnIndentStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by pana on 24/01/20.
 */
public class ReturnIndentViewModel {

    private int id;

    private String type;

    private String remarks;

    private ReturnIndentStatus status;

    List<IndentLineViewModel> indentLineList;

    private Date indentDate;

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
