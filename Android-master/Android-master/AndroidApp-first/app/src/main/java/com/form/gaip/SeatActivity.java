package com.form.gaip;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SeatActivity extends AppCompatActivity {
    String strId,strName,strMoney,strPC,strTime,pcName,finalresult,RealPcName;
    private ListView listViewSeatUse;
    private SeatListAdapter adapter;
    private List<Seat> seatList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        Button btngoback=findViewById(R.id.goback);

        Intent intent = getIntent();
        strId = intent.getStringExtra("userID");
        strName = intent.getStringExtra("userName");
        strMoney = intent.getStringExtra("userMoney");
        strPC = intent.getStringExtra("userPC");
        strTime = intent.getStringExtra("userTime");
        pcName = intent.getStringExtra("pcName");
        RealPcName=intent.getStringExtra("RealPcName");
        listViewSeatUse=findViewById(R.id.listViewSeatUse);


        seatList = new ArrayList<Seat>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.
        adapter = new SeatListAdapter(getApplicationContext(), seatList,this);
        listViewSeatUse.setAdapter(adapter);

        listViewSeatUse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(seatList.get(i).getused().equalsIgnoreCase("1")){
                    Toast.makeText(getApplicationContext(), "사용중인 좌석입니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MoneyActivity.class);
                    intent.putExtra("userID", MenuPage.AAA);
                    intent.putExtra("userName", MenuPage.BBB);
                    intent.putExtra("userMoney", MenuPage.CCC);
                    intent.putExtra("userTime", strTime);
                    intent.putExtra("userPC", finalresult);
                    intent.putExtra("seat_num",seatList.get(i).getseat_num());
                    intent.putExtra("RealPcName",RealPcName);
                    startActivity(intent);
                    finish();
                }
            }
        });

        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(pcName);


            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            String seat_num,name,used;

            //JSON 배열 길이만큼 반복문을 실행
            while(count < jsonArray.length()){
                //count는 배열의 인덱스를 의미
                JSONObject object = jsonArray.getJSONObject(count);

                seat_num = object.getString("seat_num");//여기서 ID가 대문자임을 유의
                name = object.getString("name");
                used = object.getString("used");

                //값들을 User클래스에 묶어줍니다
                Seat Seat = new Seat(seat_num, name, used);
                seatList.add(Seat);//리스트뷰에 값을 추가해줍니다
                count++;
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        btngoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserofPcroom2().execute(strId);
            }
        });
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
            finalresult=result;
            Intent intent = new Intent(getApplicationContext(), MenuPage.class);
            intent.putExtra("userID", strId);
            intent.putExtra("userName", strName);
            intent.putExtra("userMoney", strMoney);
            intent.putExtra("userTime", strTime);
            intent.putExtra("userPC", finalresult);
            startActivity(intent);
            finish();
        }


    }
    }
