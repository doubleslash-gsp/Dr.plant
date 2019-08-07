package com.example.jh.raiseplant.DrCheck_Code;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jh.raiseplant.Constants;
import com.example.jh.raiseplant.HttpClient;
import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class QuestionListFragment extends Fragment {

    private ListView question_listview;
    private QuestionListAdapter questionlist_adapter;

    private ImageButton ib_back;

    //fragment 반환
    public static QuestionListFragment newInstance() {
        return new QuestionListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_questionlist, container, false);


        ib_back = (ImageButton) rootview.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

        questionlist_adapter = new QuestionListAdapter(); // Adapter 생성
        question_listview = (ListView) rootview.findViewById(R.id.lv_questionlistview);
        question_listview.setAdapter(questionlist_adapter);
        question_listview.requestFocusFromTouch();
        question_listview.setSelection(questionlist_adapter.getCount() - 1);

        question_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionInfo questionInfo = (QuestionInfo) parent.getAdapter().getItem(position);

                ((MainActivity)getActivity()).replaceFragment(
                        ReplycheckFragment.newInstance(questionInfo.getPlant_num(), questionInfo.getReplay(),
                                                        questionInfo.getNum(), questionInfo.getContent(), questionInfo.getUser_id()));
            }
        });

        getQuestionListTask gettask = new getQuestionListTask();
        gettask.execute();

        return rootview;
    }

    class getQuestionListTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/selectBoard2.do");

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
                        String title = jObject.getString("title");
                        String user_id = jObject.getString("user_id");
                        String date =  jObject.getString("date");
                        int reply = jObject.getInt("reply");
                        int plant_num = jObject.getInt("plant_num");
                        int board_num = jObject.getInt("board_num");
                        String content = jObject.getString("content");

                        questionlist_adapter.addItem(title, user_id, date, reply, board_num, plant_num, content, user_id);

                    }
                    questionlist_adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "질문 목록 가져오기 성공", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

