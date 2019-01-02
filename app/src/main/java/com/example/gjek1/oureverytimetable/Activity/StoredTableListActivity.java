package com.example.gjek1.oureverytimetable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gjek1.oureverytimetable.BasicStructure.StoredTable;
import com.example.gjek1.oureverytimetable.ListView.StoredTableAdapater;
import com.example.gjek1.oureverytimetable.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StoredTableListActivity extends AppCompatActivity {
    private Button btn_load, btn_delete;
    private StoredTableAdapater storedTableAdapater;
    private ArrayList<StoredTable> storedTables;
    private Gson gson;
    private Type listType;
    private String id;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storedtable_list);

        // gson 빌더
        gson = new GsonBuilder().create();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // 객체 로드
        btn_load = findViewById(R.id.btn_load);
        btn_delete = findViewById(R.id.btn_delete);

        storedTables = new ArrayList<StoredTable>();
        SharedPreferences sp = getSharedPreferences("ourTimeTable", MODE_PRIVATE);
        id = sp.getString("id","");
        String strContact = sp.getString(id, "");

        listType = new TypeToken<ArrayList<StoredTable>>() {
        }.getType();

        storedTables = gson.fromJson(strContact, listType);

        if (storedTables == null) {
            storedTables = new ArrayList<StoredTable>();
        }

        storedTableAdapater = new StoredTableAdapater(getApplicationContext());

        ListView lv = (ListView) findViewById(R.id.listView_loadtable);
        lv.setAdapter(storedTableAdapater);
        storedTableAdapater.setSotredTableListItem(storedTables);

        storedTableAdapater.notifyDataSetChanged();
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                // Item은 타이틀만 가지고 있자나 -> 이것의 포지션을 가지고 잇어야해!
                // 포지션 int형만 넘겨받아서 현재
                intent.putExtra("loadResult", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Item은 타이틀만 가지고 있자나 -> 이것의 포지션을 가지고 잇어야해!
                // 포지션 int형만 넘겨받아서 현재
                if (storedTableAdapater.getSelectCount() != 1) {
                    if (storedTableAdapater.getSelectCount() > 1) {
                        Log.e("getSelectPostion ", "> 1");
                        Toast toast = Toast.makeText(getApplicationContext(), "한개만 선택해주세요", Toast.LENGTH_LONG);
                        toast.show();
                        // 한개만 선택
                    } else {
                        Log.e("getSelectPostion ", "< 1");
                        Toast toast = Toast.makeText(getApplicationContext(), "불러올 시간표를 선택해주세요", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else if (storedTableAdapater.getSelectCount() == 1) {
                    intent.putExtra("result", "load");
                    intent.putExtra("selectPosition", storedTableAdapater.getSelectPosition());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (storedTableAdapater.getSelectCount() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "한개 이상 선택해주세요", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Boolean[] selectArray = storedTableAdapater.getSelectArray();
                    int length = selectArray.length;

                    for (int i = length - 1; i >= 0; i--) {
                        if (selectArray[i]) {
                            storedTables.remove(i);
                        }
                    }

                    String json = gson.toJson(storedTables, listType);

                    SharedPreferences sp = getSharedPreferences("ourTimeTable", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(id, json); // JSON으로 변환한 객체를 저장한다.
                    editor.apply();

                    Toast toast = Toast.makeText(getApplicationContext(), "삭제 되었습니다", Toast.LENGTH_LONG);
                    toast.show();
                    intent.putExtra("result", "delete");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
