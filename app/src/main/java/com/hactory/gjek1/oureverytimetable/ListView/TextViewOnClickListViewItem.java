package com.hactory.gjek1.oureverytimetable.ListView;

public class TextViewOnClickListViewItem {
    private String personName;
    private String subject;
    private String professor;
    private String time;
    private String day;
    private String startTime;
    private String endTime;
    private String place;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDay() {
        return day;
    }

    public void setDay(int d) {
        String week[]={"월", "화", "수", "목", "금", "토", "일"};
        this.day = week[d];
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(int start) {
        int hour, minute;
        hour = start/12;
        minute = (start%12)*5;
        this.startTime = Integer.toString(hour)+":"+(minute<10 ? "0" : "")+Integer.toString(minute);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(int end) {
        int hour, minute;
        hour = end/12;
        minute = (end%12)*5;
        this.endTime = Integer.toString(hour)+":"+(minute<10 ? "0" : "")+Integer.toString(minute);

        setTime(getDay()+" "+getStartTime()+"~"+getEndTime());
    }
}
