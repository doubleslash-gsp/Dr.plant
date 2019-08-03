package com.example.jh.raiseplant.MyPlant_Code;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.raiseplant.Constants;
import com.example.jh.raiseplant.HttpClient;
import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.jh.raiseplant.R.drawable;
import static com.example.jh.raiseplant.R.id;
import static com.example.jh.raiseplant.R.layout;

public class DiaryPlantFragment extends Fragment implements View.OnClickListener {

    private ImageButton cancle, modify;
    private ImageView diary_plantpic;
    private EditText diary_plantname, diary_plantkind, diary_plantkindEn, last_water;
    private TextView firstdate, temperature, humi, dateplus;

    //오늘 연월일,시간
    public int mYear, mMonth, mDay;
    private long d, t, r;

    //디데이 연월일 변수
    private int dYear, dMonth, dDay;
    private int resultNumber=0;

    private boolean modify_mode =false;
    private String getname, getkind, getkindeng, getfirsday, getlastwater;
    private Integer gethumi, gettemper;

    private int getnum; //식물 고유 번호

    public static DiaryPlantFragment newInstance(int num) {
        Bundle args = new Bundle();
        DiaryPlantFragment fragment = new DiaryPlantFragment();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    //get parameters, set plant imformation(json object)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getnum = getArguments().getInt("num");

            //get plant json string
            SharedPreferences sp = getActivity().getSharedPreferences("plant_object", Activity.MODE_PRIVATE);
            Log.i("error_test", Integer.toString(getnum));

            String plant = sp.getString(Integer.toString(getnum), null);
            Log.i("error_test", "object: " + plant);

            JSONObject jObject = null;
            if(plant!=null){
                try {
                    jObject = new JSONObject(plant.toString());

                    getname = jObject.getString("name");
                    getkind = jObject.getString("kind");
                    getkindeng =  jObject.getString("kind_eng");
                    getfirsday =  jObject.getString("firstday");
                    getlastwater =  jObject.getString("lastwater");
                    gethumi = jObject.getInt("humidity");
                    gettemper = jObject.getInt("temper");

                } catch (JSONException e) {
                    e.printStackTrace();
                } //end try catch
            }

        }
    } //end oncreate

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout.fragment_plantdiary, container, false);

        cancle = (ImageButton) rootView.findViewById(id.cancle);
        cancle.setOnClickListener(this);

        modify = (ImageButton) rootView.findViewById(id.modify);
        modify.setOnClickListener(this);

        diary_plantpic = (ImageView)rootView.findViewById(id.diary_plantpic); //식물 사진

        dateplus = (TextView) rootView.findViewById(id.dateplus);

        diary_plantname = (EditText) rootView.findViewById(id.diary_plantname);
        diary_plantname.setText(getname); //식물이름

        diary_plantkind = (EditText)rootView.findViewById(id.diary_plantkind);
        diary_plantkind.setText(getkind);//식물종류

        diary_plantkindEn = (EditText)rootView.findViewById(id.diary_plantkindEn);
        if(getkindeng != null){
            diary_plantkindEn.setText(getkindeng); //식물 종류 영어
        }

        last_water = (EditText) rootView.findViewById(id.last_water);
        if(getlastwater != null){
            last_water.setText(getlastwater);//마지막 물 준 날짜
        }

        temperature = (TextView)rootView.findViewById(id.temperature);
        if(gettemper!=null)
            temperature.setText(Integer.toString(gettemper));//식물 온도

        humi = (TextView) rootView.findViewById(id.humi);
        if(humi!=null)
            humi.setText(Integer.toString(gethumi));//식물 토양 습도

        firstdate = (EditText) rootView.findViewById(id.et_firstday); //구매날짜
        if(getfirsday != null) {
            firstdate.setText(getfirsday);

            if(getfirsday.length()==10){
                dYear = Integer.parseInt(getfirsday.substring(0, 4));
                dMonth = Integer.parseInt(getfirsday.substring(5, 7));
                dDay = Integer.parseInt(getfirsday.substring(8));

                // 디데이 계산
                // 현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);

                Log.i("error_test", "date: " + dYear + dMonth +dDay);


                Calendar dCal = Calendar.getInstance();
                dCal.set(dYear,dMonth,dDay);

                t=cal.getTimeInMillis(); //오늘 날짜를 밀리타임으로 바꿈
                d=dCal.getTimeInMillis(); //디데이날짜를 밀리타임으로 바꿈
                r=(d-t)/(24*60*60*1000);  //디데이 날짜에서 오늘 날짜를 뻰 값을 '일'단위로 바꿈

                resultNumber=(int)r+1;
                updateDisplay();
            }else{
                Toast.makeText(getContext(), "구매 날짜를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }




        return rootView;
    }
    private void updateDisplay(){
        if(resultNumber>=0){
            dateplus.setText(String.format("-%d",resultNumber));
        }
        else{
            int absR=Math.abs(resultNumber);
            dateplus.setText(String.format("+%d",absR));
        }
    } //디데이 날짜가 오늘날짜보다 뒤에오면 '-', 앞에 오면 '+'

    private boolean EditMode(boolean flag){
        boolean reflag = !flag;

        Log.i("error_test", "reflag flag': " + reflag);
        if (reflag){ //수정모드
            modify.setImageResource(drawable.okay);
            diary_plantname.setEnabled(true);//식물이름
            diary_plantname.setFocusable(true);
            diary_plantname.setFocusableInTouchMode(true);
            diary_plantkind.setEnabled(true);//식물 종류
            diary_plantkind.setFocusable(true);
            diary_plantkind.setFocusableInTouchMode(true);
            diary_plantkindEn.setEnabled(true);//식물종류(영어)
            diary_plantkindEn.setFocusable(true);
            diary_plantkindEn.setFocusableInTouchMode(true);
            firstdate.setEnabled(true); //시작날짜(구매날짜)
            firstdate.setFocusable(true);
            firstdate.setFocusableInTouchMode(true);
            last_water.setEnabled(true);//마지막으로 물 준 날짜
            last_water.setFocusable(true);
            last_water.setFocusableInTouchMode(true);
            Toast.makeText(getContext(), "수정 가능합니다.", Toast.LENGTH_SHORT).show();
        } else{ //비수정모드
            modify.setImageResource(drawable.modify);
            diary_plantname.setEnabled(false); //식물이름
            diary_plantname.setFocusable(false);
            diary_plantname.setFocusableInTouchMode(false);
            diary_plantkind.setEnabled(false); //식물종류
            diary_plantkind.setFocusable(false);
            diary_plantkind.setFocusableInTouchMode(false);
            diary_plantkindEn.setEnabled(false); //식물종류(영어)
            diary_plantkindEn.setFocusable(false);
            diary_plantkindEn.setFocusableInTouchMode(false);
            firstdate.setEnabled(false); //시작날짜(구매날짜)
            firstdate.setFocusable(false);
            firstdate.setFocusableInTouchMode(false);
            last_water.setEnabled(false);//마지막으로 물 준 날짜
            last_water.setFocusable(false);
            last_water.setFocusableInTouchMode(false);
            updatePlantInfo();
        }
        return reflag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify:
                modify_mode = EditMode(modify_mode);
                Log.i("error_test", "modify flag': " + modify_mode);
                break;
            case id.cancle:
                ((MainActivity)getActivity()).onBackPressed();
                break;
        }
    }

    private void updatePlantInfo(){
        //식물번호, 종류, 종류(영어), 구매날짜, 마지막물준날짜, 사용자 고유값

        Map<String, String> param = new HashMap<String, String>();
        param.put("num", Integer.toString(getnum)); //사용자고유값
        param.put("name", diary_plantname.getText().toString()); //식물이름
        param.put("kind", diary_plantkind.getText().toString()); //식물종류
        param.put("kind_eng", diary_plantkindEn.getText().toString()); //식물종료(영어)
        param.put("firstday", firstdate.getText().toString());
        param.put("lastwater", last_water.getText().toString());

        updatePlantTask update = new updatePlantTask();
        update.execute(param);

    }

    class updatePlantTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/updatePlant.do");
            http.addAllParameters(maps[0]);
            // HTTP 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        /** * @param s : doInBackground에서 리턴한 body */
        @Override
        protected void onPostExecute(String s) {

            JSONObject result = null;
            try {
                result = new JSONObject(s);

                if(result.getString("result").equals("Success")){
                    Toast.makeText(getActivity(), "식물 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
