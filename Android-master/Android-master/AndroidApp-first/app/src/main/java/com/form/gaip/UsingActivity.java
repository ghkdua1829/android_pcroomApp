package com.form.gaip;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UsingActivity extends Activity {
    String strId,strName,strMoney,strNum,RealPcName,strPC,strTime,pcName,Time2,finalresult2;
    int Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using);
        Intent intent = getIntent();
        strId = intent.getStringExtra("userID");
        strName = intent.getStringExtra("userName");
        strMoney = intent.getStringExtra("userMoney");
        strNum = intent.getStringExtra("seat_num");
        strTime = intent.getStringExtra("userTime");
        strPC = intent.getStringExtra("userPC");
        RealPcName = intent.getStringExtra("RealPcName");
        Time2 = intent.getStringExtra("Time");
        Time=Integer.parseInt(Time2);
        final Button exit=findViewById(R.id.exit);
        final Button MoreTime=findViewById(R.id.MoreTime);




        final TextView textView1 = (TextView)findViewById(R.id.text_view);

        CountDownTimer countDownTimer = new CountDownTimer(Time, 1000) {
            public void onTick(final long millisUntilFinished) {
                textView1.setText(String.format(Locale.getDefault(), "남은 시간 : %d 시간 %d 분 %d 초.",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//                textView1.setText(String.format(Locale.getDefault(), "남은 시간 : %d 시간 %d 분 %d 초.", millisUntilFinished/1000,millisUntilFinished/(1000*60),millisUntilFinished/(1000*60*60)));
                MoreTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)))+"시간"+
                                (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)))+"분"+(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
                                +"남으셨습니다.",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MoneyActivity2.class);
                        intent.putExtra("userID", MenuPage.AAA);
                        intent.putExtra("userName", MenuPage.BBB);
                        intent.putExtra("userMoney", strMoney);
                        intent.putExtra("seat_num", strNum);
                        intent.putExtra("userTime",String.valueOf(millisUntilFinished) );
                        intent.putExtra("RealPcName", RealPcName);

                        startActivity(intent);
                        finish();
                    }
                });

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");


                                    //서버에서 보내준 값이 true이면?
                                    if (success) {

                                        Toast.makeText(getApplicationContext(), "메인메뉴로 돌아갑니다.", Toast.LENGTH_SHORT).show();

                                        //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                        String userID = jsonResponse.getString("userID");
                                        String userName = jsonResponse.getString("userName");
                                        String userMoney = jsonResponse.getString("userMoney");
                                        String userTime = jsonResponse.getString("userTime");


                                        //로그인에 성공했으므로 MenuPage로 넘어감
                                        Intent intent = new Intent(getApplicationContext(), ExitActivity.class);
                                        intent.putExtra("userID", userID);
                                        intent.putExtra("userName", userName);
                                        intent.putExtra("userMoney", userMoney);
                                        intent.putExtra("userTime", userTime);

                                        startActivity(intent);
                                        finish();

                                    } else {//로그인 실패시
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                        builder.setMessage("로그인에 실패하셨습니다.")
                                                .setNegativeButton("retry", null)
                                                .create()
                                                .show();


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        TimeRequest TimeRequest = new TimeRequest(strId,String.valueOf(millisUntilFinished),strNum,RealPcName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(UsingActivity.this);
                        queue.add(TimeRequest);
                    }
                });
            }
            public void onFinish() {
                new UserofPcroom2().execute(strId);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            //서버에서 보내준 값이 true이면?
                            if (success) {

                                Toast.makeText(getApplicationContext(), "사용시간이 종료되었습니다.", Toast.LENGTH_SHORT).show();

                                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                String userID = jsonResponse.getString("userID");
                                String userName = jsonResponse.getString("userName");
                                String userMoney = jsonResponse.getString("userMoney");
                                String userTime = jsonResponse.getString("userTime");


                                //로그인에 성공했으므로 MenuPage로 넘어감
                                Intent intent = new Intent(getApplicationContext(), ExitActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userName", userName);
                                intent.putExtra("userMoney", userMoney);
                                intent.putExtra("userTime", userTime);

                                startActivity(intent);
                                finish();

                            } else {//로그인 실패시
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setMessage("로그인에 실패하셨습니다.")
                                        .setNegativeButton("retry", null)
                                        .create()
                                        .show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                TimeRequest TimeRequest = new TimeRequest(strId,"0",strNum,RealPcName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UsingActivity.this);
                queue.add(TimeRequest);
            }
        }.start();


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new UserofPcroom2().execute(strId);
//            }
//        }, 2000);
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
        }


    }

}
