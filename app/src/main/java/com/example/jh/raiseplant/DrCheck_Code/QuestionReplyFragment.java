package com.example.jh.raiseplant.DrCheck_Code;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.raiseplant.Constants;
import com.example.jh.raiseplant.HttpClient;
import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionReplyFragment extends Fragment implements View.OnClickListener {

    private String board_user_id, content, plant_name, plant_kind, plant_firsday;
    private int board_num;

    private TextView tv_content, tv_writer, tv_name, tv_plantkind, tv_plant_info;
    private EditText et_content_one, et_content_two;
    private ImageButton ib_back, ib_save;

    //fragment 반환
    public static QuestionReplyFragment newInstance(String board_user_id, String content, int board_num, String plant_name, String plant_kind, String plant_firsday) {
        QuestionReplyFragment questionReplyFragment = new QuestionReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("board_user_id", board_user_id);
        bundle.putString("content", content);
        bundle.putInt("board_num", board_num);
        bundle.putString("plant_name", plant_name);
        bundle.putString("plant_kind", plant_kind);
        bundle.putString("plant_firsday", plant_firsday);
        questionReplyFragment.setArguments(bundle);
        return questionReplyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            board_user_id = getArguments().getString("board_user_id");
            content = getArguments().getString("content");
            board_num = getArguments().getInt("board_num");
            plant_name = getArguments().getString("plant_name");
            plant_kind = getArguments().getString("plant_kind");
            plant_firsday = getArguments().getString("plant_firsday");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_questionreply, container, false);


        tv_content = (TextView) rootview.findViewById(R.id.tv_content);
        tv_content.setText(content); //문의내용

        SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String user_id = auto.getString("user_id", null);

        tv_writer = (TextView) rootview.findViewById(R.id.tv_writer);
        tv_writer.setText(user_id); //답변 작성자 고유값

        tv_plantkind = (TextView) rootview.findViewById(R.id.tv_plantkind);
        tv_plantkind.setText(plant_kind+"("+plant_name+")"); //식물종류(이름)

        tv_name = (TextView) rootview.findViewById(R.id.tv_name);
        tv_name.setText(plant_name); //식물이름

        tv_plant_info = (TextView) rootview.findViewById(R.id.tv_plant_info);
        tv_plant_info.setText(plant_kind+"/"+plant_firsday); //식물종류/firstday

        et_content_one = (EditText) rootview.findViewById(R.id.et_content_one);
        et_content_two = (EditText) rootview.findViewById(R.id.et_content_two);

        ib_back = (ImageButton) rootview.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        ib_save = (ImageButton) rootview.findViewById(R.id.ib_save);
        ib_save.setOnClickListener(this);

        return rootview;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ib_back:
                ((MainActivity)getActivity()).onBackPressed();
                break;
            case R.id.ib_save:
                if(et_content_one.getText().equals("") || et_content_two.getText().equals("")){
                    Toast.makeText(getContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    SaveAnswerTask saveAnswerTask = new SaveAnswerTask();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("board_num", Integer.toString(board_num));
                    params.put("content_one", et_content_one.getText().toString());
                    params.put("content_two", et_content_two.getText().toString());

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String getDateTime = sdf.format(date);
                    params.put("date_time", getDateTime);

                    params.put("user_id", tv_writer.getText().toString());
                    saveAnswerTask.execute(params);
                }
                break;

        }

    }

    class SaveAnswerTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/insertReply.do");

            http.addAllParameters(maps[0]);

            Log.i("error_test", "map: " + maps[0]);

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
                    Toast.makeText(getActivity(), "답변이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();

                }else{
                    Toast.makeText(getActivity(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
