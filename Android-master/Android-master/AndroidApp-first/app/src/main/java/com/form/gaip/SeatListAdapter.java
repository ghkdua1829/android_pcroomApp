package com.form.gaip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class SeatListAdapter extends BaseAdapter {

    private Context context;
    private List<Seat> seatList;
    private Activity parentActivity;//회원삭제 강의때 추가
    String finalresult;


    public SeatListAdapter(Context context, List<Seat> seatList,Activity parentActivity){
        this.context = context;
        this.seatList = seatList;
        this.parentActivity = parentActivity;

    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return seatList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return seatList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.seat, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌
        final TextView seat_num = (TextView)v.findViewById(R.id.seat_num);
        TextView name = (TextView)v.findViewById(R.id.name);
        Button usingButton=v.findViewById(R.id.usingButton);
        seat_num.setText(seatList.get(i).getseat_num());
        name.setText(seatList.get(i).getname());
        usingButton.setText(seatList.get(i).getButtonname(seatList.get(i).getused()));

        if(usingButton.getText().toString().equalsIgnoreCase("사용불가")){
            usingButton.setBackgroundColor(Color.RED);
        }
//            usingButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(parentActivity,"사용 불가능한 좌석입니다.",Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        else{
//            usingButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(parentActivity, SeatActivity.class);
//                    intent.putExtra("userID", MenuPage.AAA);
//                    intent.putExtra("userName", MenuPage.BBB);
//                    intent.putExtra("userMoney", MenuPage.CCC);
//                    intent.putExtra("userPC", finalresult);
//                    parentActivity.startActivity(intent);
//                }
//            });
//        }




        //만든뷰를 반환함
        return v;
    }


}