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

    static class ViewHolder{
        TextView name = null;
        CheckBox checkBox = null;
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
        View v = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_check_friend, null);
            ViewHolder holder = new ViewHolder();
            holder.name = (TextView)v.findViewById(R.id.textView_name);
            holder.checkBox = (CheckBox) v.findViewById(R.id.checkBox_);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FriendCheckListItems.get(pos).setChecked(isChecked);
                }
            });
            v.setTag(holder);
        }
        FriendCheckListItem friendCheckListItem = FriendCheckListItems.get(pos);


        if(friendCheckListItem != null) {
            ViewHolder holder = (ViewHolder)v.getTag();
            holder.name.setText(friendCheckListItem.getName());
            holder.checkBox.setChecked(friendCheckListItem.getChecked());
        }

        name.setText(friendCheckListItem.getName());
        if(friendCheckListItem.getChecked()){
            checkBox.setChecked(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FriendCheckListItems.get(pos).setChecked(isChecked);
            }
        });

/*        final CheckBox finalCheckBox = checkBox;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!FriendCheckListItems.get(pos).getChecked()){
                    finalCheckBox.setChecked(true);
                }else{
                    finalCheckBox.setChecked(false);
                }
            }
        });*/

        return v;
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
