package com.form.gaip;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.LinkAddress;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MenuPage extends AppCompatActivity {
    String strId,strName,strMoney,strTime,strPC,finalresult;
    private ListView listView;
    private UserListAdapter adapter;
    private List<Pc> pcList;
    public static String KCY,AAA,BBB,CCC,PPP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Context context = this;
        TextView tvId = findViewById(R.id.tvId);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvMoney = findViewById(R.id.tvMoney);
        TextView tvTime = findViewById(R.id.tvTime);
        Button btnLogout = findViewById(R.id.btnLogout);
        final ImageView btnCharge = findViewById(R.id.btnCharge);
        Button btnPC = findViewById(R.id.btn_PC);
        listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        strId = intent.getStringExtra("userID");
        AAA=strId;
        strName = intent.getStringExtra("userName");
        BBB=strName;
        strMoney = intent.getStringExtra("userMoney");
        CCC=strMoney;
        strTime = intent.getStringExtra("userTime");
        Long a=Long.parseLong(strTime);
        strPC = intent.getStringExtra("userPC");
        PPP=strPC;

        tvId.setText(strId);
        tvName.setText(strName);
        tvMoney.setText(strMoney);
        tvTime.setText(String.format(Locale.getDefault(), "남은 시간 : %d 시간 %d 분 %d 초.",
                TimeUnit.MILLISECONDS.toHours(a)-TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(a)), TimeUnit.MILLISECONDS.toMinutes(a)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(a)),
                TimeUnit.MILLISECONDS.toSeconds(a) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(a))));
        new BackgroundTask().execute();

        pcList = new ArrayList<Pc>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.
        adapter = new UserListAdapter(getApplicationContext(), pcList,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), pcList.get(i).getUserName()+"피시방에 연결중입니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                intent.putExtra("userID", MenuPage.AAA);
                intent.putExtra("userName", MenuPage.BBB);
                intent.putExtra("userMoney", MenuPage.CCC);
                intent.putExtra("userTime", strTime);
                intent.putExtra("pcName", pcList.get(i).getUserName());
                intent.putExtra("userPC", finalresult);
                startActivity(intent);
                finish();
            }
        });

        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(strPC);


            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            String userName;
            int seatNum;

            //JSON 배열 길이만큼 반복문을 실행
            while(count < jsonArray.length()){
                //count는 배열의 인덱스를 의미
                JSONObject object = jsonArray.getJSONObject(count);

                userName = object.getString("name");
                seatNum = object.getInt("num");
                Pc pc = new Pc(userName,Integer.toString(seatNum),null);
                pcList.add(pc);//리스트뷰에 값을 추가해줍니다
                count++;
            }


        }catch(Exception e){
            e.printStackTrace();
        }



     btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MenuPage.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        });

        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<String> ListItems = new ArrayList<>();
                ListItems.add("1000");
                ListItems.add("2000");
                ListItems.add("3000");
                ListItems.add("4000");
                ListItems.add("5000");
                final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

                final List SelectedItems  = new ArrayList();
                int defaultItem = 0;
                SelectedItems.add(defaultItem);

                AlertDialog.Builder dlg=new AlertDialog.Builder(context);
                dlg.setTitle("충전할 금액을 입력하시오;");

                dlg.setSingleChoiceItems(items, defaultItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });
                dlg.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String msg="";

                                if (!SelectedItems.isEmpty()) {
                                    int index = (int) SelectedItems.get(0);
                                    msg = ListItems.get(index);
                                    Intent intent = new Intent(MenuPage.this, ChargeActivity.class);
                intent.putExtra("userID", strId);
                intent.putExtra("userName", strName);
                intent.putExtra("userMoney", strMoney);
                intent.putExtra("chargeMoney", msg);
                intent.putExtra("userPC", strPC);
                intent.putExtra("userTime", strTime);
                startActivity(intent);
                finish();
                                }
                            }
                        });
                dlg.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                dlg.show();
//                dlg.setItems(versionArray, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        btnCharge.setText(versionArray[which]);
//                        Intent intent = new Intent(MenuPage.this, ChargeActivity.class);
//                intent.putExtra("userID", strId);
//                intent.putExtra("userName", strName);
//                intent.putExtra("userMoney", strMoney);
//                intent.putExtra("chargeMoney", versionArray[which]);
//                intent.putExtra("userPC", strPC);
//                intent.putExtra("userTime", strTime);
//                startActivity(intent);
//                finish();
//                    }
//                });
//                dlg.setPositiveButton("닫기",null);
//                dlg.show();
//                Intent intent = new Intent(MenuPage.this, ChargeActivity.class);
//                intent.putExtra("userID", strId);
//                intent.putExtra("userName", strName);
//                intent.putExtra("userMoney", strMoney);
//                intent.putExtra("userPC", strPC);
//                intent.putExtra("userTime", strTime);
//                startActivity(intent);
//                finish();
            }   // end of onclick
        });

        btnPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(finalresult!=null) {
                    Intent intent = new Intent(MenuPage.this, Pc_ChuGa.class);
                    intent.putExtra("userID", strId);
                    intent.putExtra("userName", strName);
                    intent.putExtra("userMoney", strMoney);
                    intent.putExtra("userPC", finalresult);
                    intent.putExtra("userTime", strTime);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(MenuPage.this, "한번더 누르세요.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "https://pcbangq.000webhostapp.com/pc_ChuGa.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try{
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            }catch(Exception e){
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

        }

    }



}

