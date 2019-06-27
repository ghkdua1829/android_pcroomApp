package com.form.gaip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by kch on 2018. 2. 17..
 */

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<Pc> pcList;
    private Activity parentActivity;//회원삭제 강의때 추가
    String finalresult;

    public UserListAdapter(Context context, List<Pc> pcList,Activity parentActivity){
        this.context = context;
        this.pcList = pcList;
        this.parentActivity = parentActivity;

    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return pcList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return pcList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.pc, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌
        final TextView userName = (TextView)v.findViewById(R.id.userName);
        TextView seatNum = (TextView)v.findViewById(R.id.seatNum);
        TextView location = (TextView)v.findViewById(R.id.location);
        userName.setText(pcList.get(i).getUserName());
        seatNum.setText(pcList.get(i).getseatNum());
        location.setText(pcList.get(i).getLocation());

        //삭제 버튼 객체 생성
        Button deleteButton = (Button)v.findViewById(R.id.deleteButton);


        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            //받아온 값이 success면 정상적으로 서버로부터 값을 받은 것을 의미함
                            if(success){
                                pcList.remove(i);//리스트에서 해당부분을 지워줌
                                notifyDataSetChanged();//데이터가 변경된 것을 어댑터에 알려줌
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                //volley 사용법
                //1. RequestObject를 생성한다. 이때 서버로부터 데이터를 받을 responseListener를 반드시 넘겨준다.
                //위에서 userID를 final로 선언해서 아래 처럼 가능함
                DeleteRequest deleteRequest = new DeleteRequest(userName.getText().toString(), responseListener);
                //2. RequestQueue를 생성한다.
                //여기서 UserListAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다.
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                //3. RequestQueue에 RequestObject를 넘겨준다.
                queue.add(deleteRequest);
            }//onclick
        });


        //만든뷰를 반환함
        return v;
    }

}

