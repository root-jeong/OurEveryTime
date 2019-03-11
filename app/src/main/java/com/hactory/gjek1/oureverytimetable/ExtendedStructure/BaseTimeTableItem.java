package com.hactory.gjek1.oureverytimetable.ExtendedStructure;

/* --------------- 클래스 설명 ----------------
BaseTimeTable의 BaseTableArray[][]에 들어가게되는 아이템클래스임
*/
public class BaseTimeTableItem {
    private String personName; // 사람이름
    private String professor; // 교수이름
    private int startTime; // 시작시간
    private int endTime; // 끝나느시간
    private String place; // 장소
    private String name; // 과목이름
    private int day; // 요일

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
