package com.hactory.gjek1.oureverytimetable.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.hactory.gjek1.oureverytimetable.Activity.LoginActivity;
import com.hactory.gjek1.oureverytimetable.R;

public class SearchIDDialog {

    private Context context;
    private Boolean overlap = false;

    public SearchIDDialog(Context context) {
        this.context = context;
    }

    public void showDialog(){
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_search_id);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();


        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button btn_ok = (Button) dlg.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) dlg.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "에브리타임 홈페이지로 이동합니다", Toast.LENGTH_SHORT).show();
                ((LoginActivity)context).moveLink();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }}
    // 호출할 다이얼로그 함수를 정의한다.

