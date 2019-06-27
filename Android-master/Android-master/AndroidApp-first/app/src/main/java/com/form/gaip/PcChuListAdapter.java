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

public class PcChuListAdapter extends BaseAdapter {

    private Context context;
    private List<Pc> pcList;
    private Activity parentActivity;//회원삭제 강의때 추가
    String finalresult;


    public PcChuListAdapter(Context context, List<Pc> pcList,Activity parentActivity){
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
        View v = View.inflate(context, R.layout.pc_chuga, null);
        //뷰에 다음 컴포넌트들을 연결시켜줌
        final TextView userName = (TextView)v.findViewById(R.id.userName);
        TextView seatNum = (TextView)v.findViewById(R.id.seatNum);
        TextView location = (TextView)v.findViewById(R.id.location);
        userName.setText(pcList.get(i).getUserName());
        seatNum.setText(pcList.get(i).getseatNum());
        location.setText(pcList.get(i).getLocation());

        v.setTag(pcList.get(i).getUserName());
        //삭제 버튼 객체 생성
        Button insertButton = v.findViewById(R.id.insertButton);


        insertButton.setOnClickListener(new View.OnClickListener(){
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
                Toast.makeText(parentActivity, userName.getText().toString()+"피시방이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                InsertRequest InsertRequest = new InsertRequest(Pc_ChuGa.KCY,userName.getText().toString(), responseListener);
                //2. RequestQueue를 생성한다.
                //여기서 UserListAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다
                RequestQueue queue = Volley.newRequestQueue(parentActivity);

//                3. RequestQueue에 RequestObject를 넘겨준다.
                queue.add(InsertRequest);
//                new UserofPcroom2().execute(Pc_ChuGa.KCY);
//                if(finalresult!=null) {
//                    Intent intent = new Intent(parentActivity, MenuPage.class);
//                    intent.putExtra("userID", Pc_ChuGa.KCY);
//                    intent.putExtra("userName", Pc_ChuGa.AAA);
//                    intent.putExtra("userMoney", Pc_ChuGa.BBB);
//                    intent.putExtra("userPC", finalresult);
//                    parentActivity.startActivity(intent);
//                }
//                else
//                    Toast.makeText(parentActivity, "한번더 누르세요.", Toast.LENGTH_SHORT).show();
            }//onclick
        });


        //만든뷰를 반환함
        return v;
    }
//    class UserofPcroom2 extends AsyncTask<String, Void, String> {
//        String target;
//
//        @Override
//        protected void onPreExecute() {
//            //List.php은 파싱으로 가져올 웹페이지
//            target = "http://10.20.31.73/pc_LLogin.php";
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            try {
//                String Id = (String) params[0];
//
//                URL url = new URL(target);//URL 객체 생성
//
//                String data = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
//                //URL을 이용해서 웹페이지에 연결하는 부분
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//
//                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
//
//                wr.write(data);
//                wr.flush();
//
//                //바이트단위 입력스트림 생성 소스는 httpURLConnection
//                InputStream inputStream = httpURLConnection.getInputStream();
//
//                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String temp;
//
//                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
//                StringBuilder stringBuilder = new StringBuilder();
//
//
//                //한줄씩 읽어서 stringBuilder에 저장함
//                while ((temp = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
//                }
//
//                //사용했던 것도 다 닫아줌
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            finalresult=result;
//
//        }
//
//
//    }
}

