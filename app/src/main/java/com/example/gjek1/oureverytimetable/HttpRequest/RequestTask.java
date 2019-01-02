package com.example.gjek1.oureverytimetable.HttpRequest;

import android.content.ContentValues;
import android.os.AsyncTask;

public class RequestTask {
    private String cookie;

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void login(String userid, String password, Callback callback) {
        // URL 설정.
        String url = "http://52.78.122.74/login";

        // 파라미터 설정
        ContentValues param = new ContentValues();
        param.put("userid", userid);
        param.put("password", password);

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, param, "", callback);
        networkTask.execute();
    }

    public void getTable(String tableId, Callback callback) {
        // URL 설정.
        String url = "https://everytime.kr/find/timetable/table";

        // 파라미터 설정
        ContentValues param = new ContentValues();
        param.put("id", tableId);

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, param, cookie, callback);
        networkTask.execute();
    }

    public void getTableList(String year, String semester, Callback callback) {
        // URL 설정.
        String url = "https://everytime.kr/find/timetable/table/list/semester";

        // 파라미터 설정
        ContentValues param = new ContentValues();
        param.put("year", year);
        param.put("semester", semester);

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, param, cookie, callback);
        networkTask.execute();
    }

    public void getFriendList(Callback callback) {
        // URL 설정.
        String url = "https://everytime.kr/find/friend/list";

        // 파라미터 설정
        ContentValues param = new ContentValues();

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, param, cookie, callback);
        networkTask.execute();
    }

    public void getFriendTable(String identifier, Callback callback) {
        // URL 설정.
        String url = "https://everytime.kr/find/timetable/table/friend";

        // 파라미터 설정
        ContentValues param = new ContentValues();
        param.put("identifier", identifier);
        param.put("friendInfo", "true");

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, param, cookie, callback);
        networkTask.execute();
    }

    public void requestFriend(String userid, Callback callback) {
        // URL 설정.
        String url = "https://everytime.kr/save/friend/request";

        // 파라미터 설정
        ContentValues param = new ContentValues();
        param.put("data", userid);

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, param, cookie, callback);
        networkTask.execute();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;
        private String cookie;
        private Callback callback;

        public NetworkTask(String url, ContentValues values, String cookie, Callback callback) {
            this.url = url;
            this.values = values;
            this.cookie = cookie;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values, cookie); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == null) {
                callback.callback(null);
                return;
            }

            if(s.contains("etsid")) {
                setCookie(s);
            }
            callback.callback(s);
        }
    }
}
