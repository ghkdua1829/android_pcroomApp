package com.form.gaip;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MoneyRequest extends StringRequest {

    //현재 안드로이드앱을 에뮬레이터로 돌리므로 에뮬레이터가 설치된 서버에 있는 아파치 서버에 접근하려면
    //다음과 같이 10.0.2.2:포트번호 로 접근해야합니다 저는 8080 포트를 써서 다음과 같이 했습니다
    final static private String URL = "https://pcbangq.000webhostapp.com/pc_money.php";
    private Map<String, String> parameters;

    //생성자
    public MoneyRequest(String Id, String Money,String pcName, String seat_num,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("Id", Id);
        parameters.put("Money", Money);
        parameters.put("pcName", pcName);
        parameters.put("seat_num", seat_num);
    }

    //추후 사용을 위한 부분
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


