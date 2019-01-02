package com.example.gjek1.oureverytimetable.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gjek1.oureverytimetable.BasicStructure.Person;
import com.example.gjek1.oureverytimetable.ListView.FriendCheckListAdapter;
import com.example.gjek1.oureverytimetable.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class CheckFriendActivity extends AppCompatActivity {
    private  FriendCheckListAdapter friendCheckListAdapter;
    private ArrayList<Person> persons;
    private Button button4;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkfriend_list);

        button4 = findViewById(R.id.button4);
        Intent intent = getIntent();
        persons = (ArrayList<Person>) intent.getSerializableExtra("persons");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        friendCheckListAdapter = new FriendCheckListAdapter(getApplicationContext());

        ListView lv = (ListView)findViewById(R.id.listView_friend);
        lv.setAdapter(friendCheckListAdapter);

        friendCheckListAdapter.LoadAcitivity(persons);
        friendCheckListAdapter.notifyDataSetChanged();



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result",friendCheckListAdapter.getResult());
                for(int i = 0 ; i < friendCheckListAdapter.getResult().size(); i++){
                    Log.e("sdf",friendCheckListAdapter.getResult().get(i).getName());
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
