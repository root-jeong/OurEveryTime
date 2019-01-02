package com.example.gjek1.oureverytimetable.BasicStructure;

public class MyTimetable {
    private String id;
    private String name;
    private Boolean is_primary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(int is_primary) {
        if(is_primary == 1) {
            this.is_primary = true;
        }
        else{
            this.is_primary = false;
        }
    }
}
