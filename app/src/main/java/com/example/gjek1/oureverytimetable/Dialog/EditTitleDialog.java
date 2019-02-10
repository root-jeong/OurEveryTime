package com.example.gjek1.oureverytimetable.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gjek1.oureverytimetable.R;

public class EditTitleDialog {

    private Context context;
    private String title;
    public EditTitleDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView textView_title) {
        title = textView_title.getText().toString();

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_edit_title);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 중복 타이틀 입력후 확인시 띄울 다이얼로그
        // ----

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText editText_title = (EditText) dlg.findViewById(R.id.editText_title);
        final Button btn_ok = (Button) dlg.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) dlg.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_title.getText().length() == 0){
                    Toast.makeText(context, "입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    textView_title.setText(editText_title.getText().toString());

                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
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
    }
}
