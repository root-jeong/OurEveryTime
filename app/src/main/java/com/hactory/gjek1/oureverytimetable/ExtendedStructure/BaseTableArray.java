package com.hactory.gjek1.oureverytimetable.ExtendedStructure;

//BaseTimeTable의 baseTableArray[][]의 각 인덱스에 들어가는 ArrayItem 객체임
//즉, BaseTimeTableItem -> BaseTableArray
//BaseTableArray 객체가 BaseTimeTable 클래스의 BaseTimeArray[][]의 각 인덱스에 들어가게됨

import android.graphics.Color;

import java.util.ArrayList;

public class BaseTableArray {
    private ArrayList<BaseTimeTableItem> itemList;
    private int overRayNumber; // 몇명이 겹치는지 ( 구현완료 ) - BaseTimeTable의 setBaseTableArray의 메소드에서 처리.
    private Color color; // Textview색깔 ( 미구현 )

    public BaseTableArray(){
        itemList = new ArrayList<BaseTimeTableItem>();
        this. overRayNumber = 0;
    };

    public void AddItem(BaseTimeTableItem item) {
        itemList.add(item);
    }

    public int getOverRayNumber() {
        overRayNumber = itemList.size();
        return overRayNumber;
    }

    public void setOverRayNumber(int overRayNumber) {
        this.overRayNumber = overRayNumber;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<BaseTimeTableItem> getItemList() {
        return this.itemList;
    }
}
