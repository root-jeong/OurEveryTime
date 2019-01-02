package com.example.gjek1.oureverytimetable.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gjek1.oureverytimetable.BasicStructure.StoredTable;
import com.example.gjek1.oureverytimetable.Dialog.AddFriendDialog;
import com.example.gjek1.oureverytimetable.Dialog.EditTitleDialog;
import com.example.gjek1.oureverytimetable.Dialog.OverlapCheckDialog;
import com.example.gjek1.oureverytimetable.ExtendedStructure.BaseTableArray;
import com.example.gjek1.oureverytimetable.ExtendedStructure.BaseTimeTable;
import com.example.gjek1.oureverytimetable.ListView.DialogListAdapter;
import com.example.gjek1.oureverytimetable.BasicStructure.Friend;
import com.example.gjek1.oureverytimetable.HttpRequest.Callback;
import com.example.gjek1.oureverytimetable.HttpRequest.RequestTask;
import com.example.gjek1.oureverytimetable.BasicStructure.MyTimetable;
import com.example.gjek1.oureverytimetable.BasicStructure.Person;
import com.example.gjek1.oureverytimetable.R;
import com.example.gjek1.oureverytimetable.BasicStructure.Subject;
import com.example.gjek1.oureverytimetable.BasicStructure.SubjectData;
import com.example.gjek1.oureverytimetable.BasicStructure.TimeTable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // HTTP POST Request 보내는 객체
    private RequestTask requestTask = new RequestTask();

    // Callback 함수들
    private Callback LoadMyTableList;
    private Callback LoadMyTable;
    private Callback LoadmyFriendList;
    private Callback LoadmyFriendTable;
    private View.OnClickListener textViewListner;

    //-----------------------------------------------------------------기본 변수 설명-------------------------------------------------------------------
    // * 밑에 설명되있는 모든 변수들은 BaseTimeTable의 Person ArrayList를 완성시키기위한 임시 변수,객체 !
    // 1. 리스트에 넣기전 저장하는 임시객체 : Xml Parser에서 각태그를 만들때마다 new를 사용해서 새로운 객체를 할당해주어서. 임시로 쓰임
    // 2. 클래스들이 저장되는 임시배열 : 각 클래스들은 리스트를 멤버변수를 가지고 있는 것두있기때문에 Arraylist를 생성하고 모든값을 넣어준뒤에 그값을 set하기위해 임시로 쓰이는 ArrayList
    // 3. XML파서에서 XML태그 변수
    // 4. XML파서에서 Tag 관리를 위한 Boolean. 밑에 코드보면 START_TAG/ END_TAG에 is____ 로되있는 Boolean값이 많은데 복잡해보여도 원하는것만 고치기 쉬워집니다!
    //-------------------------------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------SharedPreparenced-------------------------------------------------------------------
    //  Root Tag : OurEveryTime
    //  id Tag : user_id
    //  pw Tag : tag
    //--------------------------------------------------------------------------------------------------------------------------------------------------

    // * 베이스테이블 객체 생성
    private BaseTimeTable baseTimeTable;

    // 1. 리스트에 넣기전 저장하는 임시객체
    private MyTimetable mytimetable;
    private Friend myFriend;
    private Person person;
    private Subject subject;
    private SubjectData subjectData;
    private TimeTable timeTable;
    private ArrayList<Subject> subjects;

    // 2. 클래스들이 저장되는 임시배열
    private ArrayList<MyTimetable> myTimeTableList = new ArrayList<MyTimetable>();
    private ArrayList<Friend> myFriendList = new ArrayList<Friend>();
    private ArrayList<SubjectData> subjectDataList = new ArrayList<SubjectData>();
    private ArrayList<TimeTable> timeTableList = new ArrayList<TimeTable>();
    private ScrollView viewContentOne;

    // 3. XML파서에서 XML태그 변수
    private String tag;

    // 4. XML파서에서 Tag 관리를 위한 Boolean.
    private Boolean isTable = false, isFriend = false, isSubject = false, isTime = false, isData = false, isUser = false;
    private Boolean overwrite = false;
    private TextView[][] textViews;
    private DialogListAdapter adapter;
    private static final int CUSTOM_DIALOG_ID = 100;
    private Dialog dialog = null;
    private MyClickListener myClickListener;
    private ImageButton btn_friendCheck;
    private ImageButton btn_tableEdit;
    private Button btn_loadList;
    private TextView textView_title;
    private ArrayList<StoredTable> storedTables;

    // 시간표 텍스트뷰 다이얼로그 변수
    private TextView textView_day;
    private TextView dialogTimeStart;
    private TextView dialogTime;

    // 기타 변수
    private int colorChoice = 0;
    private String current_title;
    private int overwritePosition;
    final static int day = 5;
    final static int classtime = 32;
    private String id;
    // gson 빌더
    private Gson gson;
    // Admob
    private AdView mAdView;
    private CheckTypesTask checkTypesTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new GsonBuilder().create();
       checkTypesTask = new CheckTypesTask();
        checkTypesTask.onPreExecute();

        LoadSharedPreparencedTable();
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");
        //Admob
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //btn_frinedCheck 정의 + 설정
        btn_friendCheck = findViewById(R.id.btn_friendCheck);
        btn_friendCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Person> persons = baseTimeTable.getPersons();
                Intent intent = new Intent(MainActivity.this, CheckFriendActivity.class);
                intent.putExtra("persons", persons);
                startActivityForResult(intent, 999);
            }
        });

        // btn_loadList  정의 + 설정
        btn_loadList = findViewById(R.id.btn_loadList);
        btn_loadList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StoredTableListActivity.class);
                intent.putExtra("storeTables", storedTables);
                startActivityForResult(intent, 888);
            }
        });

        textView_title = findViewById(R.id.textView_title);

        // btn_tableEdit  정의 + 설정
        btn_tableEdit = findViewById(R.id.btn_tableEdit);
        btn_tableEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getApplicationContext(), v);
                menu.getMenuInflater().inflate(R.menu.popup_menu_table, menu.getMenu());

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tableNameEdit:
                                tableNameEdit();
                                return true;
                            case R.id.menuItem_storeTable:
                                storeSharedPreparencdTable();
                                return true;
                            case R.id.menuItem_requestFriend:
                                addFriend();
                                return true;
                            case R.id.tableColorEdit:
                                return true;
                            case R.id.red:
                                colorChoice = 0;
                                setTableColor(colorChoice);
                                return true;
                            case R.id.yellow:
                                colorChoice = 1;
                                setTableColor(colorChoice);
                                return true;
                            case R.id.orange:
                                colorChoice = 2;
                                setTableColor(colorChoice);
                                return true;
                            case R.id.pink:
                                colorChoice = 3;
                                setTableColor(colorChoice);
                                return true;
                            case R.id.purple:
                                colorChoice = 4;
                                setTableColor(colorChoice);
                                return true;
                            case R.id.sky:
                                colorChoice = 5;
                                setTableColor(colorChoice);
                                return true;
                            case R.id.dark:
                                colorChoice = 6;
                                setTableColor(colorChoice);
                                return true;
                            case R.id.menuItem_logout:
                                logout();
                                return true;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });

        baseTimeTable = new BaseTimeTable();
        textViews = new TextView[day][classtime];
        viewContentOne = findViewById(R.id.viewContentOne);

        // textView setting
        String textViewID;
                int resID;
                int time;
                for (int i = 0; i < day; i++) {
                    for (int j = 0; j < classtime; j++) {
                        final int n = i, m = j;
                        time = 96 + (j * 6);
                        textViewID = "textView_" + i + "_" + Integer.toString(time);
                        resID = getResources().getIdentifier(textViewID, "id", getPackageName());
                textViews[i][j] = (TextView) findViewById(resID);
            }
        }

        // 로그인에서 가져온 쿠키
        requestTask.setCookie(getIntent().getStringExtra("cookie"));

        // 자신의 테이블 리스트 불러오기
        setLoadMyTableList();

        // 자신의 테이블 primary 1인거 불러오기
        setLoadMyTable();

        // 친구리스트 불러오기
        setLoadMyFreindList();

        // 친구 테이블 불러오기
        setLoadmyFriendTable();

        // MainActivity로 이동후 Paser Start 함수
        ParserExecute();
    }

    private void storeSharedPreparencdTable() {
        current_title = textView_title.getText().toString();
        checkTitleOverlap();
    }


    private void checkTitleOverlap() {
        OverlapCheckDialog customDialog = new OverlapCheckDialog(MainActivity.this);
        Type listType = new TypeToken<ArrayList<StoredTable>>() {
        }.getType();
        Boolean overlap = false;

        for (int i = 0; i < storedTables.size(); i++) {
            if (storedTables.get(i).getTitle().equals(current_title)) {
                overlap = true;
                overwritePosition = i;

                customDialog.isOverLapCheck();

                if (getOverwrite()) {
                    Log.e("customDialog", "실행");
                    break;
                }
            }
        }
        if (!overlap) {
            StoredTable storedTable = baseTimeTable.getStoredTable(current_title);
            storedTable.setColorSelectoin(colorChoice);
            storedTables.add(storedTable);
            String json = gson.toJson(storedTables, listType);

            SharedPreferences sp = getSharedPreferences("ourTimeTable", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(id, json); // JSON으로 변환한 객체를 저장한다.
            editor.apply();
        }
    }

    // MainActivity의 Oncreate시에 호출됨
    // SharedPrepareneced에 저장된 StoreTable ArrayList 가져옴 : 없다면 storedTables의 size : 0
    private void LoadSharedPreparencedTable() {
        SharedPreferences sp = getSharedPreferences("ourTimeTable", MODE_PRIVATE);
        id =   sp.getString("id", "");
        storedTables = new ArrayList<StoredTable>();
        String strContact = sp.getString(id, "");

        Type listType = new TypeToken<ArrayList<StoredTable>>() {
        }.getType();

        storedTables = gson.fromJson(strContact, listType);
        if (storedTables == null) {
            storedTables = new ArrayList<StoredTable>();
        }
    }

    // colorChoice(int)와 baseTimeTable의 저장된 정보들로 테이블 색깔 지정
    public void setTableColor(int colorNum) {
        String color[] = {"#C62917", "#212121", "#212121", "#212121", "#212121","#212121","#212121"};
        // { 에브리타임 , 검정, ... } : 추후에 색깔 정할것임

        float alpha = 1.0f;
        int checkedCount = baseTimeTable.getCheckedPersonCount();
        if (checkedCount >= 1) {
            float alphalist[] = new float[checkedCount + 1];
            for (int i = 0; i < checkedCount + 1; i++) {
                alphalist[i] = i * (alpha / checkedCount);
            }
            int overrayNum;
            for (int i = 0; i < day; i++) {
                for (int j = 0; j < classtime; j++) {
                    textViews[i][j].setBackgroundColor(Color.parseColor("#ffffff"));
                    textViews[i][j].setAlpha(1.0f);
                    overrayNum = baseTimeTable.getBaseTimeTableArrayByIndex(i, j).getOverRayNumber();
                    if (overrayNum >= 1) {
                        textViews[i][j].setBackgroundColor(Color.parseColor(color[colorNum]));
                        textViews[i][j].setAlpha(alphalist[overrayNum]);
                    }
                }
            }
        } else {
            for (int i = 0; i < day; i++) {
                for (int j = 0; j < classtime; j++) {
                    textViews[i][j].setBackgroundColor(Color.parseColor("#ffffff"));
                    textViews[i][j].setAlpha(1.0f);
                }
            }
        }
    }

    public void logout() {
        SharedPreferences pref = getSharedPreferences("ourTimeTable", MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.remove("user_id");
        editor.apply();

        Toast.makeText(getApplicationContext(), "로그아웃을 했습니다.", Toast.LENGTH_SHORT).show();

        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    // 시간표 이름 수정
    public void tableNameEdit() {
        EditTitleDialog customDialog = new EditTitleDialog(MainActivity.this);
        // 커스텀 다이얼로그를 호출한다.
        // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
        customDialog.callFunction(textView_title);
    }

    // 친구 요청
    public void addFriend() {
        AddFriendDialog customDialog = new AddFriendDialog(MainActivity.this);
        // 커스텀 다이얼로그를 호출한다.
        customDialog.callFunction(requestTask);
    }

    // MainActivity로 이동후 Paser Start 함수
    public void ParserExecute() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM", Locale.KOREA);
        String year = sdf.format(date).split("/")[0];
        String month = sdf.format(date).split("/")[1];
        String semester = "";

        if (Integer.parseInt(month) < 7) {
            semester = "1";
        } else {
            semester = "2";
        }
        requestTask.getTableList(year, semester, LoadMyTableList);
        requestTask.getFriendList(LoadmyFriendList);
    }

    protected Dialog onCreatedDialog(int id, BaseTableArray baseTableArray) {
        switch (id) {
            case CUSTOM_DIALOG_ID:
                Context c = this;
                dialog = new Dialog(c);
                dialog.setContentView(R.layout.dialog_of_textview);

                textView_day = dialog.findViewById(R.id.textView_day);
                dialogTimeStart = (TextView)dialog.findViewById(R.id.dialogTimeStart);
                dialogTime  =(TextView)dialog.findViewById(R.id.dialogTime);

                ListView listView = (ListView) dialog.findViewById(R.id.listview);

                adapter = new DialogListAdapter(getApplicationContext());

                listView.setAdapter(adapter);
                adapter.addItem(baseTableArray);
        }
        return dialog;
    }

    // 자신의 테이블 리스트 불러오기
    private void setLoadMyTableList() {
        LoadMyTableList = new Callback() {
            @Override
            public void callback(String s) {
                try {
                    // 오류처리
                    if (s == null) {
                        Log.e("[ERROR]", "LoadMyTableList");
                    }

                    Log.e("LoadMyTableList", "LoadMyTableList 호출");
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();

                    InputStream targetStream = new ByteArrayInputStream(s.getBytes());
                    parser.setInput(new InputStreamReader(targetStream, "UTF-8"));
                    int parseEvent = parser.getEventType();

                    Log.e("LoadMyTableList", "LoadMyTableList Parsing Start");
                    while (parseEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parseEvent) {
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                if (tag.equals("response")) {// XML파일 첫 Tag는 항상 response! : MyTimetable클래스 객체를 생성 > 추후에 MytimetableList에 합쳐짐.
                                } else if (tag.equals("table")) {
                                    mytimetable = new MyTimetable();
                                    isTable = true;
                                }
                                if (isTable) {
                                    mytimetable.setId(parser.getAttributeValue(null, "id"));
                                    mytimetable.setName(parser.getAttributeValue(null, "name"));
                                    mytimetable.setIs_primary(Integer.parseInt(parser.getAttributeValue(null, "is_primary")));
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                tag = parser.getName();
                                if (tag.equals("table")) {
                                    // table End_Tag를 만나면 mytimetable객체를 mytimetableList에 저장합니다.
                                    isTable = false;
                                    myTimeTableList.add(mytimetable);
                                    Log.e(String.valueOf(myTimeTableList.size()), String.valueOf(timeTableList.size()));
                                } else if (tag.equals("response")) {
                                    Log.e("LoadMyTableList", "LoadMyTableList Parsing Complete");


                                    for (int i = 0; i < myTimeTableList.size(); i++) {
                                        if (myTimeTableList.get(i).getIs_primary() == true) {
                                            requestTask.getTable(myTimeTableList.get(i).getId(), LoadMyTable);
                                            break;
                                        }
                                    }
                                }
                        }
                        parseEvent = parser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // 자신의 테이블 primary 1인거 불러오기
    private void setLoadMyTable() {
        LoadMyTable = new Callback() {
            @Override
            public void callback(String s) {
                try {
                    // 오류처리
                    if (s == null) {
                        Log.e("[ERROR]", "LoadMyTable");
                    }

                    Log.e("LoadMyTable", "LoadMyTable 호출");
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();
                    InputStream targetStream = new ByteArrayInputStream(s.getBytes());
                    parser.setInput(new InputStreamReader(targetStream, "UTF-8"));
                    int parseEvent = parser.getEventType();


                    Log.e("LoadMyTable", "LoadMyTable Parsing Start");
                    while (parseEvent != XmlPullParser.END_DOCUMENT) {

                        switch (parseEvent) {
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                if (tag.equals("response")) {
                                    person = new Person();
                                } else if (tag.equals("table")) {
                                    isTable = true;
                                    timeTable = new TimeTable();
                                    subjects = new ArrayList<Subject>();
                                } else if (tag.equals("subject")) {
                                    subject = new Subject();
                                    isSubject = true;
                                } else if (tag.equals("time")) {
                                    subjectDataList = new ArrayList<SubjectData>();
                                    isTime = true;
                                } else if (tag.equals("data")) {
                                    isData = true;
                                    subjectData = new SubjectData();
                                }


                                if (isTable) {
                                    timeTable.setTableId(parser.getAttributeValue(null, "id"));
                                    if (isSubject) {
                                        if (tag.equals("subject"))
                                            subject.setSubjectId(Integer.parseInt(parser.getAttributeValue(null, "id")));
                                        else if (tag.equals("internal"))
                                            subject.setInternal(parser.getAttributeValue(null, "value"));
                                        else if (tag.equals("name"))
                                            subject.setName(parser.getAttributeValue(null, "value"));
                                        else if (tag.equals("professor"))
                                            subject.setProfessor(parser.getAttributeValue(null, "value"));
                                        else if (tag.equals("time"))
                                            subject.setTime(parser.getAttributeValue(null, "value"));

                                        if (isData) {
                                            subjectData.setPlace(parser.getAttributeValue(null, "place"));
                                            subjectData.setEndTime(Integer.parseInt(parser.getAttributeValue(null, "endtime")));
                                            subjectData.setStartTime(Integer.parseInt(parser.getAttributeValue(null, "starttime")));
                                            subjectData.setDay(Integer.parseInt(parser.getAttributeValue(null, "day")));
                                        }
                                    }
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                tag = parser.getName();
                                if (tag.equals("table")) {
                                    isTable = false;
                                    timeTable.setSubjects(subjects);
                                    person.setTimeTable(timeTable);
                                } else if (tag.equals("time")) {
                                    isTime = false;
                                    subject.setData(subjectDataList);
                                } else if (tag.equals("data")) {
                                    isData = false;
                                    subjectDataList.add(subjectData);
                                } else if (tag.equals("subject")) {
                                    isSubject = false;
                                    subjects.add(subject);
                                } else if (tag.equals("response")) {
                                    Log.e("END_TAG:response", "MytableList 파싱완료");
                                    person.setChecked(false);
                                    person.setName("나(본인)");
                                    baseTimeTable.addPerson(person);
                                }
                        }
                        parseEvent = parser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // 친구리스트 불러오기
    private void setLoadMyFreindList() {
        LoadmyFriendList = new Callback() {
            @Override
            public void callback(String s) {
                try {
                    // 오류처리
                    if (s == null) {
                        Log.e("[ERROR]", "LoadMyFriendList");
                    }

                    Log.e("LoadmyFriendList", "LoadmyFriendList 호출");
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();
                    Log.e("s", s);
                    InputStream targetStream = new ByteArrayInputStream(s.getBytes());
                    parser.setInput(new InputStreamReader(targetStream, "UTF-8"));
                    int parseEvent = parser.getEventType();

                    Log.e("LoadmyFriendList", "LoadmyFriendList Parsing Start");
                    while (parseEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parseEvent) {
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                Log.e("tag", tag);
                                if (tag.equals("response")) {
                                } else if (tag.equals("friend")) {
                                    myFriend = new Friend();
                                    isFriend = true;
                                }
                                if (isFriend) {
                                    myFriend.setFriendid(parser.getAttributeValue(null, "friendid"));
                                    myFriend.setName(parser.getAttributeValue(null, "name"));
                                    myFriend.setUserid(parser.getAttributeValue(null, "userid"));
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                tag = parser.getName();
                                if (tag.equals("friend")) {
                                    isFriend = false;
                                    myFriendList.add(myFriend);
                                } else if (tag.equals("response")) {
                                    Log.e("END_TAG:response", "MyFriendList 파싱완료");
                                    for (int i = 0; i < myFriendList.size(); i++) {
                                        requestTask.getFriendTable(myFriendList.get(i).getUserid(), LoadmyFriendTable);
                                    }
                                }
                        }
                        parseEvent = parser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // 친구 테이블 불러오기
    private void setLoadmyFriendTable() {
        LoadmyFriendTable = new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void callback(String s) {
                try {
                    // 오류처리
                    if (s == null) {
                        Log.e("[ERROR]", "LoadMyFriendTable");
                    }

                    Log.e("LoadmyFriendTable", "LoadmyFriendTable 호출");
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();
                    InputStream targetStream = new ByteArrayInputStream(s.getBytes());
                    parser.setInput(new InputStreamReader(targetStream, "UTF-8"));
                    int parseEvent = parser.getEventType();

                    Log.e("LoadMyTable", "LoadMyTable Parsing Start");
                    while (parseEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parseEvent) {
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                if (tag.equals("response")) {
                                    person = new Person();
                                } else if (tag.equals("table")) {
                                    isTable = true;
                                    timeTable = new TimeTable();
                                    subjects = new ArrayList<Subject>();
                                } else if (tag.equals("subject")) {
                                    subject = new Subject();
                                    isSubject = true;
                                } else if (tag.equals("time")) {
                                    subjectDataList = new ArrayList<SubjectData>();
                                    isTime = true;
                                } else if (tag.equals("data")) {
                                    isData = true;
                                    subjectData = new SubjectData();
                                } else if (tag.equals("user")) {
                                    isUser = true;
                                }


                                if (isTable) {
                                    timeTable.setTableId(parser.getAttributeValue(null, "id"));
                                    if (isSubject) {
                                        if (tag.equals("subject"))
                                            subject.setSubjectId(Integer.parseInt(parser.getAttributeValue(null, "id")));
                                        else if (tag.equals("internal"))
                                            subject.setInternal(parser.getAttributeValue(null, "value"));
                                        else if (tag.equals("name"))
                                            subject.setName(parser.getAttributeValue(null, "value"));
                                        else if (tag.equals("professor"))
                                            subject.setProfessor(parser.getAttributeValue(null, "value"));
                                        else if (tag.equals("time"))
                                            subject.setTime(parser.getAttributeValue(null, "value"));

                                        if (isData) {
                                            subjectData.setPlace(parser.getAttributeValue(null, "place"));
                                            subjectData.setEndTime(Integer.parseInt(parser.getAttributeValue(null, "endtime")));
                                            subjectData.setStartTime(Integer.parseInt(parser.getAttributeValue(null, "starttime")));
                                            subjectData.setDay(Integer.parseInt(parser.getAttributeValue(null, "day")));
                                        }
                                    }
                                } else if (isUser) {
                                    person.setName(parser.getAttributeValue(null, "name"));
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                tag = parser.getName();
                                if (tag.equals("table")) {
                                    isTable = false;
                                    timeTable.setSubjects(subjects);
                                    person.setTimeTable(timeTable);
                                    Log.e("END_TAG:table", "Mytable 파싱완료");
                                } else if (tag.equals("time")) {
                                    isTime = false;
                                    subject.setData(subjectDataList);
                                } else if (tag.equals("data")) {
                                    isData = false;
                                    subjectDataList.add(subjectData);
                                } else if (tag.equals("subject")) {
                                    isSubject = false;
                                    subjects.add(subject);
                                } else if (tag.equals("user")) {
                                    isUser = false;
                                } else if (tag.equals("response")) {
                                    Log.e("END_TAG:response", "FriendTable 파싱완료");
                                    person.setChecked(false);
                                    baseTimeTable.addPerson(person);

                                }
                        }
                        parseEvent = parser.next();


                    }

                    if (myFriendList.size() + 1 == baseTimeTable.persons.size()) {
                        Void result = null;
                        checkTypesTask.onPostExecute(result);
                        baseTimeTable.setBaseTableArray();
                        Log.e("setBaseTableArray", "setBaseTableArray");
                        for (int i = 0; i < day; i++) {
                            for (int j = 0; j < classtime; j++) {
                                myClickListener = new MyClickListener(i, j);
                                textViews[i][j].setOnClickListener(myClickListener);
                            }
                        }
//                        setBasetimetableColor();
                        setTableColor(colorChoice);
                    }

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    // textView 클릭 이벤트 커스텀 온클릭
    private class MyClickListener implements View.OnClickListener {

        int day;
        int time;

        public MyClickListener(int day, int time) {
            this.day = day;
            this.time = time;
        }
        String start() {
            int h=time/2+8;
            int m=(time%2)*30;
            return Integer.toString(h)+":"+(m<10 ? "0" : "")+Integer.toString(m);
        }
        String end() {
            int h=(time+1)/2+8;
            int m=((time+1)%2)*30;
            return Integer.toString(h)+":"+(m<10 ? "0" : "")+Integer.toString(m);
        }

        @Override
        public void onClick(View v) {
            onCreatedDialog(CUSTOM_DIALOG_ID, baseTimeTable.getBaseTimeTableArrayByIndex(day, time)).show();

            String[] days = {"월","화","수","목","금"};
            textView_day.setText(days[day]);
            dialogTimeStart.setText(start());
            dialogTime.setText(start()+" ~ "+end());

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Window window = dialog.getWindow();
            int x = (int) (size.x * 0.8f);
            int y = (int) (size.y * 0.6f);
            window.setLayout(x, y);
        }
    }


    // intent로 다른액티비티를 호출하고 다시 intent로 결과값을 받을때 실행되는 함수
    // intent의 코드값을 같이 보내기 때문에 받을때도 code값의 case에 따라 처리해주면 됨.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // intent를 보내고 다시 돌려받을 때 정해진 버튼 또는 규칙에 따라서만 결과값을 처리하게됨.
        if (resultCode != RESULT_OK) {
            Toast.makeText(MainActivity.this, "버튼을 통해 적용됩니다.", Toast.LENGTH_LONG).show();
            return;
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 999 code : 친구체크 리스트뷰 액티비티 에서 intent를 넘겨받앗을때의 코드
                case 999:
                    ArrayList<Person> persons = (ArrayList<Person>) data.getSerializableExtra("result");
                    baseTimeTable.setPersonsChecked(persons);
                    baseTimeTable.setBaseTableArray();
                    setTableColor(colorChoice);
                    break;
                // 888 code : 저장된 테이블 불러오기 액티비티에서 intent를 넘겨받았을때의 코드
                case 888:
                    String resultTag = data.getStringExtra("result");
                    if(resultTag.equals("load")) {
                        LoadSharedPreparencedTable();
                        int selectPosition = data.getIntExtra("selectPosition", -1);
                        baseTimeTable.setPersonsCheckedByStoredTable(storedTables.get(selectPosition));
                        baseTimeTable.setBaseTableArray();
                        textView_title.setText(storedTables.get(selectPosition).getTitle());
                        colorChoice = storedTables.get(selectPosition).getColorSelectoin();
                        setTableColor(colorChoice);

                    }else if(resultTag.equals("delete")){
                        LoadSharedPreparencedTable();
                    }
                    break;
            }
        }
    }

    // OverLapCheck 다이얼로그의 Ok/cancle button의 온클릭와 연동하구위한 함수 - 다이얼로그의 Ok의 버튼 클릭시 호출됨.
    public void setOverwrite() {
        Type listType = new TypeToken<ArrayList<StoredTable>>() {
        }.getType();
        storedTables.get(overwritePosition).setStoredPerson(baseTimeTable.getStoredTable(current_title).getStoredPerson());
        storedTables.get(overwritePosition).setColorSelectoin(colorChoice);
        String json = gson.toJson(storedTables, listType);

        SharedPreferences sp = getSharedPreferences("ourTimeTable", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(id, json);
        editor.apply();
    }

    public Boolean getOverwrite() {
        return overwrite;
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                MainActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");
            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}


