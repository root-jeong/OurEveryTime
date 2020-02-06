package com.hactory.gjek1.oureverytimetable.ExtendedStructure;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.hactory.gjek1.oureverytimetable.BasicStructure.Person;
import com.hactory.gjek1.oureverytimetable.BasicStructure.StoredPerson;
import com.hactory.gjek1.oureverytimetable.BasicStructure.StoredTable;

import java.util.ArrayList;


public class BaseTimeTable {
    final static int day = 5;
    final static int classtime = 32;
    public ArrayList<Person> persons; // Parser 완료후 0번index : 본인 / 나머지 : 친구
    private BaseTimeTableItem baseTimeTableItem; // BaseTableArray[][] 배열에 들어가는 임시 아이템클래스 객체
    private BaseTableArray[][] baseTableArrays; // 월화수목금(5) x 26교시(26)
    //private String[] days = {"월","화","수","목","금"};//  0번은 Null값으로 두고 [1][1] = [월요일][1교시] / [5][26] = [금요일][26교시]  / 그래서 배열의크기 : [6][27]
    private StoredTable storedTable;

    public BaseTimeTable() {
        persons = new ArrayList<Person>();
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setBaseTableArray() {
        this.baseTableArrays = new BaseTableArray[day][classtime];

        for (int i = 0; i < day; i++) {
            for (int j = 0; j < classtime; j++) {
                this.baseTableArrays[i][j] = new BaseTableArray();
                /*baseTableArrays[i+1][j+1].setColor(Color.valueOf(Color.BLACK));*/
            }
        }

        for (int i = 0; i < day; i++) {
            for (int startTime = 96; startTime <= 282; startTime = (startTime + 6)) {
                BaseTableArray baseTableArray = new BaseTableArray();
                for (int personNum = 0; personNum < persons.size(); personNum++) {
                    if (persons.get(personNum).getChecked() == true) {
                        if (persons.get(personNum).getTimeTable() == null) continue;
                        for (int subjectNum = 0; subjectNum < persons.get(personNum).getTimeTable().getSubjects().size(); subjectNum++) {
                            for (int subjectDataNum = 0; subjectDataNum < persons.get(personNum).getTimeTable().getSubjects().get(subjectNum).getData().size(); subjectDataNum++) {
                                baseTimeTableItem = new BaseTimeTableItem();
                                baseTimeTableItem.setPersonName(persons.get(personNum).getName());
                                baseTimeTableItem.setProfessor(persons.get(personNum).getTimeTable().getSubjects().get(subjectNum).getProfessor());
                                baseTimeTableItem.setStartTime(persons.get(personNum).getTimeTable().getSubjects().get(subjectNum).getData().get(subjectDataNum).getStartTime());
                                baseTimeTableItem.setEndTime(persons.get(personNum).getTimeTable().getSubjects().get(subjectNum).getData().get(subjectDataNum).getEndTime());
                                baseTimeTableItem.setDay(persons.get(personNum).getTimeTable().getSubjects().get(subjectNum).getData().get(subjectDataNum).getDay());
                                baseTimeTableItem.setPlace(persons.get(personNum).getTimeTable().getSubjects().get(subjectNum).getData().get(subjectDataNum).getPlace());
                                baseTimeTableItem.setName(persons.get(personNum).getTimeTable().getSubjects().get(subjectNum).getName());
                                if (baseTimeTableItem.getStartTime() == startTime && baseTimeTableItem.getDay() == i) {
                                    for (int z = baseTimeTableItem.getStartTime(); z < baseTimeTableItem.getEndTime(); z = (z + 6)) {
                                        baseTableArrays[i][(startTime - 96) / 6 + (z - baseTimeTableItem.getStartTime()) / 6].AddItem(baseTimeTableItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        setBaseTableArraysOverrayNum();
    }

    public void setBaseTableArraysOverrayNum() {
        for (int i = 0; i < day; i++) {
            for (int j = 0; j < classtime; j++) {
                baseTableArrays[i][j].setOverRayNumber(baseTableArrays[i][j].getItemList().size());
            }
        }
    }

//    public void setBaseTableColor(Context context){
//        TextView textViews[][];
//        textViews = new TextView[day][classtime];
//
//        View convertview = new View(context);
//
//
//        String textViewID;
//        int resID;
//        int time;
//        for (int i = 0; i < day; i++) {
//            for (int j = 0; j < classtime; j++) {
//                final int n = i, m = j;
//                time = 96 + (j * 6);
//                textViewID = "textView_" + i + "_" + Integer.toString(time);
//                resID = context.getResources().getIdentifier(textViewID, "id", context.getPackageName());
//                textViews[i][j] = (TextView) convertview.findViewById(resID);
//            }
//        }
//
//    }

    public BaseTableArray getBaseTimeTableArrayByIndex(int column, int row) {
        return baseTableArrays[column][row];
    }

    //    public BaseTableArray getBaseTimeTableArrayItemSizeByIndex(int column, int row) {
//        return baseTableArrays[column][row];
//    }
    public int getPersonsize() {
        int size = 0;
        for (int i = 0; i < persons.size(); i++) {
            size += 1;
        }
        return size;
    }

    public ArrayList<Person> getPersons() {
        return this.persons;
    }

    public void setPersonsChecked(ArrayList<Person> persons) {
        for (int i = 0; i < persons.size(); i++) {
            this.persons.get(i).setChecked(persons.get(i).getChecked());
        }
    }

    public int getCheckedPersonCount() {
        int count = 0;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getChecked() == true) {
                count += 1;
            }

        }
        return count;
    }

    public StoredTable getStoredTable(String title) {
        ArrayList<StoredPerson> storedPersons = new ArrayList<StoredPerson>();
        StoredPerson storedPerson;
        StoredTable storedTable = new StoredTable();
        for (int i = 0; i < getPersonsize(); i++) {
            storedPerson = new StoredPerson();
            storedPerson.setChecked(persons.get(i).getChecked());
            storedPerson.setName(persons.get(i).getName());
            storedPersons.add(storedPerson);
        }
        storedTable.setTitle(title);
        storedTable.setStoredPerson(storedPersons);

        return storedTable;
    }

//    public void setTableByStoredTable(StoredTable storedTable){
//        // TODO : 쉐어드피레어런스에서 가져온 스토어테이블 탐색하면서 checked 값 person에 대입
//        // 현재는 어레이리스트로 테이블이 저장되지않고 하나만 저장되는형태
//        // 추후에는 저장된 목록을 볼 수 있는 액티비티(리스트뷰 + 어댑터) 형식으로 만들고, 저장할때도 같은 새로운타이틀이면 add되서 저장되고/ 타이틀이 중복된게있으면 기존에있던 타이틀에 저장(즉, 수정)
//        // 삭제기능도 추가해야댐
//
//        ArrayList<StoredPerson> storedPersons;
//        storedPersons = storedTable.getStoredPerson();
//        for(int i = 0;  i< getPersonsize(); i++){
//            persons.get(i).setChecked(storedPersons.get(i).isChecked());
//        }
//    }

    public void setPersonsCheckedByStoredTable(StoredTable storedTable) {
        this.storedTable = storedTable;
        Log.e("persons size", Integer.toString(persons.size()));
        Log.e("getPersonsize()", Integer.toString(getPersonsize()));
        for (int i = 0; i < getPersonsize(); i++) {
            persons.get(i).setChecked(this.storedTable.getStoredPerson().get(i).isChecked());
        }
    }
}
