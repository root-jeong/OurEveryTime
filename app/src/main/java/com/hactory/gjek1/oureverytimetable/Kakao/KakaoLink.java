package com.hactory.gjek1.oureverytimetable.Kakao;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;



import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class KakaoLink {

    public KakaoLink() {
    }

    // 공유하기 링크를 보내는 함수
    public void sendLink(Context context, String userId) {
        TextTemplate params = TextTemplate.newBuilder("뭐듣누에서 에브리타임 친구요청을 보냈습니다.",
                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").setAndroidExecutionParams("userId=" + userId).build())
                .setButtonTitle("친구요청 수락하기").build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(context, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });
    }

    // 인텐트로부터 query를 뽑아내어 roomKey를 얻어 받환한다.
    public String checkLink(Intent intent) {
        if (intent != null) {
            Uri uri = intent.getData();

            if (uri != null) {
                String query = uri.getQuery();

                if (query != null) {
                    String userId = query.split("=")[1];
                    Log.e("[Intent Data]", userId);
                    return userId;
                }
            }
        }
        return null;
    }

    // [디버그용] : 카카오 개발자 키해시 등록을 위한 함수
    public String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("[Key Hash]", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}
