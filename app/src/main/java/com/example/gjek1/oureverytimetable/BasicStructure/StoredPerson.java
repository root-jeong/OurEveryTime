package com.example.gjek1.oureverytimetable.BasicStructure;

import android.os.Parcel;
import android.os.Parcelable;

public class StoredPerson implements Parcelable {
    private String name;
    private boolean checked;

    public  StoredPerson(){}

    protected StoredPerson(Parcel in) {
        name = in.readString();
        checked = in.readByte() != 0;
    }

    public static final Creator<StoredPerson> CREATOR = new Creator<StoredPerson>() {
        @Override
        public StoredPerson createFromParcel(Parcel in) {
            return new StoredPerson(in);
        }

        @Override
        public StoredPerson[] newArray(int size) {
            return new StoredPerson[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (checked ? 1 : 0));
    }
}
