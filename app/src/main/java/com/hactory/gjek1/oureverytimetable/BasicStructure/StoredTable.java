package com.hactory.gjek1.oureverytimetable.BasicStructure;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class StoredTable implements Parcelable {
    private String title;
    private ArrayList<StoredPerson> StoredPerson;
    private int colorSelectoin;


    public StoredTable(){}

    protected StoredTable(Parcel in) {
        title = in.readString();
    }

    public static final Creator<StoredTable> CREATOR = new Creator<StoredTable>() {
        @Override
        public StoredTable createFromParcel(Parcel in) {
            return new StoredTable(in);
        }

        @Override
        public StoredTable[] newArray(int size) {
            return new StoredTable[size];
        }
    };

    public ArrayList<com.hactory.gjek1.oureverytimetable.BasicStructure.StoredPerson> getStoredPerson() {
        return StoredPerson;
    }

    public void setStoredPerson(ArrayList<StoredPerson> storedPerson) {
        StoredPerson = storedPerson;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    public int getColorSelectoin() {
        return colorSelectoin;
    }

    public void setColorSelectoin(int colorSelectoin) {
        this.colorSelectoin = colorSelectoin;
    }
}
