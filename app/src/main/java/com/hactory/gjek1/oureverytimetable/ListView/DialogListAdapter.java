package com.hactory.gjek1.oureverytimetable.ListView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hactory.gjek1.oureverytimetable.ExtendedStructure.BaseTableArray;
import com.hactory.gjek1.oureverytimetable.R;

import java.util.ArrayList;

public class DialogListAdapter extends BaseAdapter {
    private ArrayList<TextViewOnClickListViewItem> textViewOnClickListViewItemList;
    private Context context;
    //private int overRayNumber; // 몇명이 겹치는지 ( 구현완료 ) - BaseTimeTable의 setBaseTableArray의 메소드에서 처리.
    private Color color;


    public DialogListAdapter(Context context) {
        this.context = context;
        textViewOnClickListViewItemList = new ArrayList<TextViewOnClickListViewItem>();
    }

    @Override
    public int getCount() {
        return textViewOnClickListViewItemList.size();
    }

    @Override
    public TextViewOnClickListViewItem getItem(int position) {
        return textViewOnClickListViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        TextView name = null;
        TextView subject = null;
        TextView professor = null;
        TextView time = null;
        TextView place = null;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_dialog_of_textview, parent, false);
        }

        name = convertView.findViewById(R.id.name);
        subject = convertView.findViewById(R.id.subject);
        professor = convertView.findViewById(R.id.professor);
        time = convertView.findViewById(R.id.time);
        place = convertView.findViewById(R.id.place);

        TextViewOnClickListViewItem textViewOnClickListViewItem = textViewOnClickListViewItemList.get(pos);
        name.setText(textViewOnClickListViewItem.getPersonName());
        subject.setText(textViewOnClickListViewItem.getSubject());
        professor.setText(textViewOnClickListViewItem.getProfessor());
        time.setText(textViewOnClickListViewItem.getTime());
        place.setText(textViewOnClickListViewItem.getPlace());

        return convertView;
    }

//    public int getOverRayNumber() {
//        return overRayNumber;
//    }

//    public void setOverRayNumber(int overRayNumber) {
//        this.overRayNumber = overRayNumber;
//    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void addItem(BaseTableArray baseTableArray) {
        TextViewOnClickListViewItem item;
        textViewOnClickListViewItemList = new ArrayList<TextViewOnClickListViewItem>();

        for (int i = 0; i < baseTableArray.getItemList().size(); i++) {
            item = new TextViewOnClickListViewItem();
            item.setPersonName(baseTableArray.getItemList().get(i).getPersonName());
            item.setPlace(baseTableArray.getItemList().get(i).getPlace());
            item.setProfessor(baseTableArray.getItemList().get(i).getProfessor());
            item.setSubject(baseTableArray.getItemList().get(i).getName());
            item.setDay(baseTableArray.getItemList().get(i).getDay());
            item.setStartTime(baseTableArray.getItemList().get(i).getStartTime());
            item.setEndTime(baseTableArray.getItemList().get(i).getEndTime());
            textViewOnClickListViewItemList.add(item);
        }

    }
}
