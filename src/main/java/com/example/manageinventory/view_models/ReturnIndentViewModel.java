package com.example.manageinventory.view_models;

import java.util.Date;
import java.util.List;

/**
 * Created by pana on 24/01/20.
 */
public class ReturnIndentViewModel {

    private int id;

    private String type;

    List<IndentLineViewModel> indentLines;

    private Date indentDate;

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

    public Date getIndentDate() {
        return indentDate;
    }

    public void setIndentDate(Date indentDate) {
        this.indentDate = indentDate;
    }
}
