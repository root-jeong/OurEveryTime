package com.hactory.gjek1.oureverytimetable.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hactory.gjek1.oureverytimetable.HttpRequest.Callback;
import com.hactory.gjek1.oureverytimetable.HttpRequest.RequestTask;
import com.hactory.gjek1.oureverytimetable.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class AddFriendDialog {

    private Context context;
    private String tag;
    public AddFriendDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final RequestTask requestTask) {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_edit_title);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 중복 타이틀 입력후 확인시 띄울 다이얼로그
        final Callback callback = new Callback() {
            @Override
            public void callback(String s) {
                String result = "";
                int response = -1;

                Log.e("[LOG]value", s);

                try {
                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();

                    InputStream targetStream = new ByteArrayInputStream(s.getBytes());
                    parser.setInput(new InputStreamReader(targetStream, "UTF-8"));
                    int parseEvent = parser.getEventType();

                    while (parseEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parseEvent) {
                            case XmlPullParser.START_TAG:
                                tag = parser.getName();
                                if(tag.equals("response")) {
                                    response = Integer.parseInt(parser.nextText());
                                    Log.e("response", parser.nextText());
                                }
                                break;
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

                Log.e("[LOG]END", Integer.toString(response));
                if (response == 1) {
                    result = "친구 요청을 보냈습니다.\n상대방이 수락하면 친구가 맺어집니다.";
                } else if (response == -1) {
                    result = "올바르지 않은 상대입니다.";
                } else if (response == -2) {
                    result = "이미 친구인 상대입니다.";
                } else if (response == -3) {
                    result = "이미 친구 요청을 보낸 상대입니다.\n상대방이 수락하면 친구가 맺어집니다.";
                } else {
                    result = "친구 요청을 할 수 없습니다.";
                }

                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        };


        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText editText_title = (EditText) dlg.findViewById(R.id.editText_title);
        final Button btn_ok = (Button) dlg.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) dlg.findViewById(R.id.btn_cancel);
        final TextView tv_title = (TextView) dlg.findViewById(R.id.title);

        editText_title.setHint("에브리타임 친구 아이디를 입력해주세요");
        tv_title.setText("친구 요청");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_title.getText().length() == 0){
                    Toast.makeText(context, "입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    requestTask.requestFriend(editText_title.getText().toString(), callback);

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
