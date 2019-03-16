package com.hactory.gjek1.oureverytimetable.ListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hactory.gjek1.oureverytimetable.BasicStructure.Person;
import com.hactory.gjek1.oureverytimetable.R;

import java.util.ArrayList;

public class FriendCheckListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendCheckListItem> FriendCheckListItems;
    private ArrayList<Person> persons;

    public FriendCheckListAdapter(Context context){
        FriendCheckListItems = new ArrayList<FriendCheckListItem>();
        this.context = context;
    }
    @Override
    public int getCount() {
        return FriendCheckListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return FriendCheckListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        TextView name = null;
        CheckBox checkBox = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_check_friend, null);
        }
        name = convertView.findViewById(R.id.textView_name);
        checkBox = convertView.findViewById(R.id.checkBox_);

        FriendCheckListItem friendCheckListItem = FriendCheckListItems.get(pos);
        name.setText(friendCheckListItem.getName());

        if(FriendCheckListItems.get(pos).getChecked()){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox finalCheckBox = v.findViewById(R.id.checkBox_);

                if(!finalCheckBox.isChecked()){
                    finalCheckBox.setChecked(true);

                }else{
                    finalCheckBox.setChecked(false);
                }

                FriendCheckListItems.get(pos).setChecked(finalCheckBox.isChecked());
            }
        });

        return convertView;
    }
    public void LoadAcitivity(ArrayList<Person> persons) {
        FriendCheckListItem friendCheckListItem;
        this.persons = persons;
        for(int i = 0 ; i < persons.size(); i++){
            friendCheckListItem = new FriendCheckListItem();
            friendCheckListItem.setChecked(persons.get(i).getChecked());
            friendCheckListItem.setName(persons.get(i).getName());
            FriendCheckListItems.add(friendCheckListItem);
            Log.e(persons.get(i).getName(),"추가됨");
        }
    }

    public ArrayList<Person> getResult(){
        for(int i = 0 ; i < persons.size(); i++){
            persons.get(i).setChecked(FriendCheckListItems.get(i).getChecked());
        }

        return this.persons;
    }

}
