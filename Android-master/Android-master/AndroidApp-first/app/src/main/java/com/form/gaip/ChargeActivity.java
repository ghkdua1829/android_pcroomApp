package com.form.gaip;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ChargeActivity extends AppCompatActivity {
    String strId,strName,strMoney,strPC,strTime,chargeMoney;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

//        final EditText charge=findViewById(R.id.charge);
//        Button btngoback=findViewById(R.id.goback);
//        Button btngocharge=findViewById(R.id.gocharge);

        Intent intent = getIntent();
        strId = intent.getStringExtra("userID");
        strName = intent.getStringExtra("userName");
        strMoney = intent.getStringExtra("userMoney");
        strTime = intent.getStringExtra("userTime");
        strPC = intent.getStringExtra("userPC");
        strPC = intent.getStringExtra("userPC");
        chargeMoney= intent.getStringExtra("chargeMoney");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            //서버에서 보내준 값이 true이면?
                            if (success) {

                                Toast.makeText(getApplicationContext(), chargeMoney+"원 충전 하셨습니다.", Toast.LENGTH_SHORT).show();

                                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                String userMoney = jsonResponse.getString("userMoney");


                                //로그인에 성공했으므로 MenuPage로 넘어감

                                Intent intent = new Intent(getApplicationContext(), MenuPage.class);
                                intent.putExtra("userID", strId);
                                intent.putExtra("userName", strName);
                                intent.putExtra("userMoney", userMoney);
                                intent.putExtra("userTime", strTime);
                                intent.putExtra("userPC", strPC);
                                startActivity(intent);
                                finish();
                                ChargeActivity.this.startActivity(intent);

                            } else {//충전 실패시
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChargeActivity.this);
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
                ChargeRequest ChargeRequest = new ChargeRequest(strId, chargeMoney, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChargeActivity.this);
                queue.add(ChargeRequest);
            }
        }, 500);
    }
//        btngoback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MenuPage.class);
//                intent.putExtra("userID", strId);
//                intent.putExtra("userName", strName);
//                intent.putExtra("userMoney", strMoney);
//                intent.putExtra("userTime", strTime);
//                intent.putExtra("userPC", strPC);
//
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        btngocharge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                new BackgroundTask().execute(strId,charge.getText().toString());
//
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//
//
//                            //서버에서 보내준 값이 true이면?
//                            if (success) {
//
//                                Toast.makeText(getApplicationContext(), charge.getText().toString()+"원 충전 하셨습니다.", Toast.LENGTH_SHORT).show();
//
//                                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//
//                                String userMoney = jsonResponse.getString("userMoney");
//
//
//                                //로그인에 성공했으므로 MenuPage로 넘어감
//
//                                Intent intent = new Intent(getApplicationContext(), MenuPage.class);
//                                intent.putExtra("userID", strId);
//                                intent.putExtra("userName", strName);
//                                intent.putExtra("userMoney", userMoney);
//                                intent.putExtra("userTime", strTime);
//                                intent.putExtra("userPC", strPC);
//                                startActivity(intent);
//                                finish();
//                                ChargeActivity.this.startActivity(intent);
//
//                            } else {//충전 실패시
//                                AlertDialog.Builder builder = new AlertDialog.Builder(ChargeActivity.this);
//                                builder.setMessage("충전에 실패하셨습니다.")
//                                        .setNegativeButton("retry", null)
//                                        .create()
//                                        .show();
//
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                ChargeRequest ChargeRequest = new ChargeRequest(strId, charge.getText().toString(), responseListener);
//                RequestQueue queue = Volley.newRequestQueue(ChargeActivity.this);
//                queue.add(ChargeRequest);
//            }
//        });
}

