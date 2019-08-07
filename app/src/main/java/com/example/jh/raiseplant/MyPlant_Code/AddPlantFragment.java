package com.example.jh.raiseplant.MyPlant_Code;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.example.jh.raiseplant.Constants;
import com.example.jh.raiseplant.HttpClient;
import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPlantFragment extends Fragment implements View.OnClickListener{

    private KeyboardWatcher keyboardWatcher;
    private ImageButton backAdd, completeAdd;
    private ImageView addplantpic;
    private EditText newplantname, newplantkind, newplantwater, newplantdate;

    public static AddPlantFragment newInstance() {
        AddPlantFragment fragment = new AddPlantFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addplant, container, false);

        backAdd = (ImageButton) rootView.findViewById(R.id.backAdd);
        backAdd.setOnClickListener(this);

        completeAdd = (ImageButton) rootView.findViewById(R.id.completeAdd);
        completeAdd.setOnClickListener(this);

        addplantpic = (ImageView) rootView.findViewById(R.id.addplantpic);
        addplantpic.setOnClickListener(this); // end clicklistener()

        newplantname = (EditText) rootView.findViewById(R.id.newplantname);
        newplantkind = (EditText) rootView.findViewById(R.id.newplantkind);
        newplantwater = (EditText) rootView.findViewById(R.id.newplantwater);
        newplantdate = (EditText) rootView.findViewById(R.id.newplantdate);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backAdd:
                ((MainActivity)getActivity()).onBackPressed();
                Log.d("fragment_change", "AddplanFragment back button click");
                break;
            case R.id.completeAdd:

                //사용자가 입력한 내용을 가져오기
                String strname = newplantname.getText().toString();
                String strkind = newplantkind.getText().toString();
                String strwater = newplantwater.getText().toString();
                String strdate = newplantdate.getText().toString();

                if(strname.equals("") || strkind.equals("") || strwater.equals("") || strdate.equals(""))
                    Toast.makeText(getContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else{
                    addPlantTask task = new addPlantTask();
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("name", strname);
                    param.put("kind", strkind);
                    param.put("water", strwater);
                    param.put("firstday", strdate);
                    SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    String user_id = auto.getString("user_id", null);
                    param.put("user_id", user_id);
                    task.execute(param);
                }
                break;
            case R.id.addplantpic:
                //카메라 추가
                break;
        }
    }


    class addPlantTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/insertPlant.do");
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
                    Toast.makeText(getActivity(), "식물 저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).onBackPressed();
                }else{
                    Toast.makeText(getActivity(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
