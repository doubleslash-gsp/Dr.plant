package com.example.jh.raiseplant.DrCheck_Code;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.aphidmobile.flip.FlipViewController;
import com.example.jh.raiseplant.Constants;
import com.example.jh.raiseplant.HttpClient;
import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//http://www.devexchanges.info/2015/11/page-flip-animation-in-android.html

public class ReplycheckFragment extends Fragment implements View.OnClickListener {


    private FlipViewController flipViewController;
    private ReplycheckListAdapter flipadapter;
    private ArrayList<ReplyInfo> replyList;

    private TextView tv_content, tv_name, tv_plantkind, tv_plant_info;
    private ImageButton ib_add, ib_back;

    //질문 정보
    private int reply;
    private int board_num;
    private String content;
    private String user_id;

    //질문 내에 있는 식물 정보
    private int plant_num;
    private String getname, getkind, getfirsday;

    public static ReplycheckFragment newInstance(int plant_num, int reply, int board_num, String content, String user_id) {
        ReplycheckFragment replycheckFragment = new ReplycheckFragment();
        Bundle params = new Bundle();
        params.putInt("plant_num", plant_num);
        params.putInt("reply", reply);
        params.putInt("board_num", board_num);
        params.putString("content", content);
        params.putString("user_id", user_id);
        replycheckFragment.setArguments(params);
        return replycheckFragment;
    }
    //식물번호,,, 이름, 종류, 물주기, 온두, 습도, 구매날짜, 마지막으로 물 준 날


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plant_num = getArguments().getInt("plant_num");
            reply = getArguments().getInt("reply");
            board_num = getArguments().getInt("board_num");
            content = getArguments().getString("content");
            user_id = getArguments().getString("user_id"); //질문 작성자 고유값
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_replycheck
                , container, false);

        tv_content = (TextView) rootview.findViewById(R.id.tv_content);
        tv_content.setText(Html.fromHtml(content,  Html.	FROM_HTML_MODE_LEGACY)); //식물 증상

        tv_plantkind = (TextView) rootview.findViewById(R.id.tv_plantkind);
        tv_name = (TextView) rootview.findViewById(R.id.tv_name);
        tv_plant_info = (TextView) rootview.findViewById(R.id.tv_plant_info);

        ib_add = (ImageButton) rootview.findViewById(R.id.ib_add);
        ib_add.setOnClickListener(this);

        ib_back = (ImageButton)rootview.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);

        //답변 플립 애니메이션 세팅
        flipViewController = (FlipViewController) rootview.findViewById(R.id.flip_view);
        replyList = new ArrayList<>();

        getPlantTask plantTask = new getPlantTask();
        Map<String, String> args = new HashMap<String, String>();
        args.put("plant_num", Integer.toString(plant_num));
        plantTask.execute(args);

        if(reply>0){
            Map<String, String> params = new HashMap<String, String>();
            //답변이 있으면 답변 가져오기
            // if reply>0: 질문번호,,,,진단내용, 처방내역, 답변작성날짜, 답변작성자(사용자고유값)

            getReplyListTask getReplyListTask = new getReplyListTask();
            params.put("board_num", Integer.toString(board_num));
            getReplyListTask.execute(params);
        }


        //create and attach adapter to flipper view
        flipadapter = new ReplycheckListAdapter(((MainActivity)getContext()), replyList);
        flipViewController.setAdapter(flipadapter);

        return rootview;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_add:
                ((MainActivity)getActivity()).replaceFragment(QuestionReplyFragment.newInstance(user_id, content, board_num, getname, getkind, getfirsday));
                break;
            case R.id.ib_back:
                ((MainActivity)getActivity()).onBackPressed();
                break;

        }
    }

    class getPlantTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/SelectPlantWithQuestion.do");
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

                    getname = result.getString("name");
                    getkind = result.getString("kind");
                    getfirsday = result.getString("firstday");

                    Log.i("error_test", result.toString());

                    tv_plantkind.setText(getkind+"("+getname+")"); //식물종류(이름)
                    tv_name.setText(getname); //식물이름
                    tv_plant_info.setText(getkind+"/"+getfirsday); //식물종류/firstday

                }else{
                    Toast.makeText(getActivity(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getReplyListTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/selectReply.do");
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

                    JSONArray jarray = new JSONObject(s).getJSONArray("object");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jObject = jarray.getJSONObject(i);
                        ReplyInfo replyInfo = new ReplyInfo();
                        replyInfo.setContent_one(jObject.getString("content_one"));
                        replyInfo.setContent_two(jObject.getString("content_two"));
                        replyInfo.setWriter(jObject.getString("writer"));
                        replyInfo.setDate_time(jObject.getString("date_time"));
                        replyList.add(replyInfo);
                    }
                    flipadapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(), "답변 목록 가져오기 성공", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
