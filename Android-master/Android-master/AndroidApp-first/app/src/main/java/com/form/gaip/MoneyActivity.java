package com.form.gaip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MoneyActivity extends AppCompatActivity {
    String strId,strName,strMoney,strNum,strTime,RealPcName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        Intent intent = getIntent();
        strId = intent.getStringExtra("userID");
        strName = intent.getStringExtra("userName");
        strMoney = intent.getStringExtra("userMoney");
        strTime = intent.getStringExtra("userTime");
        strNum = intent.getStringExtra("seat_num");
        RealPcName = intent.getStringExtra("RealPcName");
        Toast.makeText(getApplicationContext(), strTime, Toast.LENGTH_SHORT).show();
        final Button one=(Button)findViewById(R.id.one);
        final Button two=(Button)findViewById(R.id.two);
        final Button three=(Button)findViewById(R.id.three);
        final Button four=(Button)findViewById(R.id.four);
        final Button five=(Button)findViewById(R.id.five);
        final Button zero=(Button)findViewById(R.id.zero);
//        Button goback=(Button)findViewById(R.id.goback);

        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.one:
                        onClick1(one);
                        break;
                    case R.id.two:
                        onClick1(two);
                        break;
                    case R.id.three:
                        onClick1(three);
                        break;
                    case R.id.four:
                        onClick1(four);
                        break;
                    case R.id.five:
                        onClick1(five);
                        break;
                    case R.id.zero:
                        onClick1(zero);
                        break;
                }
            }
        };
        one.setOnClickListener(listener);
        two.setOnClickListener(listener);
        three.setOnClickListener(listener);
        four.setOnClickListener(listener);
        five.setOnClickListener(listener);
        zero.setOnClickListener(listener);

    }

    public void onClick1(final View view) {

        final Button newButton=(Button) view;
        final int A=Integer.parseInt(newButton.getText().toString());
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    //서버에서 보내준 값이 true이면?
                    if (success) {

                        Toast.makeText(getApplicationContext(), newButton.getText().toString()+" 사용하셨습니다.", Toast.LENGTH_SHORT).show();

                        String userMoney = jsonResponse.getString("userMoney");
//                                String userTime = jsonResponse.getString("userTime");
//                                String Time=String.valueOf(Integer.valueOf(userTime)+3600000);
                        String Time=String.valueOf(Integer.valueOf(strTime)+3600*A);
                        Intent intent = new Intent(getApplicationContext(), UsingActivity.class);
                        intent.putExtra("userID", strId);
                        intent.putExtra("userName", strName);
                        intent.putExtra("seat_num", strNum);
                        intent.putExtra("userMoney", userMoney);
                        intent.putExtra("RealPcName", RealPcName);
                        intent.putExtra("Time", Time);
                        startActivity(intent);
                        finish();

                    } else {//충전 실패시
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage("충전에 실패하셨습니다.")
                                .setNegativeButton("retry", null)
                                .create()
                                .show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        if(Integer.parseInt(strMoney)>=Integer.parseInt(newButton.getText().toString())) {
            MoneyRequest MoneyRequest = new MoneyRequest(strId, newButton.getText().toString(), RealPcName, strNum, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(MoneyRequest);
        }
        else {
            Toast.makeText(getApplicationContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
        }
    }
}