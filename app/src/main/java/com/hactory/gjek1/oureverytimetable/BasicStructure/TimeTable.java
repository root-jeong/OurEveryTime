package com.hactory.gjek1.oureverytimetable.BasicStructure;

import java.util.ArrayList;

public class TimeTable {
    private ArrayList<Subject> subjects;
    private String tableId;

    public TimeTable() {

    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
