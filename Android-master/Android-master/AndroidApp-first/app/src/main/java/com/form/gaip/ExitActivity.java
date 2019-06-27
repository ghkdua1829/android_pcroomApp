package com.form.gaip;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ExitActivity extends Activity {
    String strId, strName, strMoney, strPC, strTime, pcName, finalresult2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        Intent intent = getIntent();
        strId = intent.getStringExtra("userID");
        strName = intent.getStringExtra("userName");
        strMoney = intent.getStringExtra("userMoney");
        strTime = intent.getStringExtra("userTime");
        new UserofPcroom2().execute(strId);


    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    class UserofPcroom2 extends AsyncTask<String, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "https://pcbangq.000webhostapp.com/pc_LLogin.php";
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String Id = (String) params[0];

                URL url = new URL(target);//URL 객체 생성

                String data = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());

                wr.write(data);
                wr.flush();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();


                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            finalresult2=result;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MenuPage.class);
                    intent.putExtra("userID", strId);
                    intent.putExtra("userName", strName);
                    intent.putExtra("userMoney", strMoney);
                    intent.putExtra("userTime", strTime);
                    intent.putExtra("userPC", finalresult2);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }


    }
}