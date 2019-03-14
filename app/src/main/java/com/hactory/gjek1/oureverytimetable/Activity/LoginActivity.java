package com.hactory.gjek1.oureverytimetable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hactory.gjek1.oureverytimetable.Dialog.SearchIDDialog;
import com.hactory.gjek1.oureverytimetable.HttpRequest.Callback;
import com.hactory.gjek1.oureverytimetable.HttpRequest.RequestTask;
import com.hactory.gjek1.oureverytimetable.Kakao.KakaoLink;
import com.hactory.gjek1.oureverytimetable.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id;
    private EditText et_pw;
    private CheckBox cb_autoLogin;

    // HTTP POST Request 보내는 객체
    private RequestTask requestTask = new RequestTask();

    // 콜백 함수
    private Callback callback;

    // 카카오링크
    private KakaoLink kakaoLink = new KakaoLink();
    private String addFriend;

    // 자동 로그인
    private SharedPreferences pref;
    private String id;
    private String pw;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 객체 가져오기
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        cb_autoLogin = findViewById(R.id.cb_autoLogin);

        // Admob 초기화
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // 카카오링크 키해시
        Log.e("[hash]", kakaoLink.getKeyHash(getApplicationContext()));

        addFriend = kakaoLink.checkLink(getIntent());
        if(addFriend == null) {
            Log.e("[카카오링크]", "링크 없이 앱실행");
        } else {
            Log.e("[카카오링크]", "링크로 앱실행");
        }

        // 로그인 콜백
        callback = new Callback() {
            @Override
            public void callback(String s) {
                // 오류처리
                if(s == null) {
                    // 자동 로그인 초기화
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e("[로그인]", s);

                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                myIntent.putExtra("cookie", s);
                myIntent.putExtra("user_id", id);
                myIntent.putExtra("addFriend", addFriend);
                startActivity(myIntent);
                finish();
            }
        };

        // 자동 로그인 체크
        pref = getSharedPreferences("ourTimeTable", MODE_PRIVATE);
        id = pref.getString("user_id", "");
        pw = pref.getString("pw", "");

        // 자동 로그인 상태라면 로그인 진행
        if(!id.equals("")) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            et_id.setText(id);
            et_pw.setText(pw);
            cb_autoLogin.setChecked(true);
            requestTask.login(id, pw, callback);
        }
    }

    public void onLoginButtonClicked(View v) {
        id = et_id.getText().toString();
        pw = et_pw.getText().toString();

        if(cb_autoLogin.isChecked()) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("user_id", id);
            editor.putString("pw", pw);
            editor.apply();
        }
        requestTask.login(id, pw, callback);
    }
    public void onSearchButtonClicked(View v) {
        showDialog();
    }
    public void showDialog(){
        SearchIDDialog dialog = new SearchIDDialog(this);
        dialog.showDialog();
    }

    public void moveLink(){
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://everytime.kr/forgot"));
        startActivity(myIntent);
    }
}
