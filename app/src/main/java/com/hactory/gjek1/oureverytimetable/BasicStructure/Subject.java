package com.hactory.gjek1.oureverytimetable.BasicStructure;

import java.util.ArrayList;

public class Subject {
    private int subjectId;
    private String internal;//학수번호
    private String name;
    private String professor;
    private String time;
    private ArrayList<SubjectData> datas;

    public Subject() {
        datas = new ArrayList<>();
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<SubjectData> getData() {
        return datas;
    }

    public void setData(ArrayList<SubjectData> data) {
        this.datas = data;
    }

}