package com.hactory.gjek1.oureverytimetable.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hactory.gjek1.oureverytimetable.BasicStructure.StoredTable;
import com.hactory.gjek1.oureverytimetable.R;

import java.util.ArrayList;

public class StoredTableAdapater extends BaseAdapter {
    private Context context;
    private ArrayList<StoredTableListViewItem> storedTableListViewItems;
    private int selectPosition;
    private Boolean array[];
    private int selectCount;

    public StoredTableAdapater(Context context){
        storedTableListViewItems = new ArrayList<StoredTableListViewItem>();
        this.context = context;
        selectPosition = -1;
        selectCount = 0;
        array = new Boolean[getCount()];
    }
    @Override
    public int getCount() {
        return storedTableListViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return storedTableListViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        TextView title = null;
        CheckBox checkBox = null;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_storedtable_list, null);
        }
        title = convertView.findViewById(R.id.textView_title);
        checkBox = convertView.findViewById(R.id.checkBox_);

        final StoredTableListViewItem storedTableListViewItem = storedTableListViewItems.get(pos);
        title.setText(storedTableListViewItem.getTitle());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    array[pos] = true;
                    selectCount++;
                }else{
                    array[pos] = false;
                    selectCount--;
                }
            }
        });
        final CheckBox finalCheckBox = checkBox;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!array[pos]){
                    finalCheckBox.setChecked(true);
                }else{
                    finalCheckBox.setChecked(false);
                }
            }
        });
        return convertView;
    }

    public void setSotredTableListItem(ArrayList<StoredTable> storedTables) {
        StoredTableListViewItem storedTableListViewItem;
        for(int i = 0 ; i < storedTables.size(); i++){
            storedTableListViewItem = new StoredTableListViewItem();
            storedTableListViewItem.setTitle(storedTables.get(i).getTitle());
            storedTableListViewItems.add(storedTableListViewItem);

        }
        array = new Boolean[getCount()];
        for(int i = 0 ; i < array.length; i++){
            array[i] = false;
        }
    }


    public int getSelectCount(){
        return selectCount;
    }
    public int getSelectPosition(){
        for(int i = 0 ; i < array.length; i++){
            if(array[i]){
                return i;
            }
        }
        return selectPosition;
    }
    public  Boolean[] getSelectArray(){
        return array;
    }
}
