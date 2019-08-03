package com.example.jh.raiseplant.MyPlant_Code;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jh.raiseplant.Constants;
import com.example.jh.raiseplant.HttpClient;
import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPlantFragment extends Fragment implements View.OnClickListener {

    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageButton addPlant;


    //fragment 반환
    public static MyPlantFragment newInstance() {
        return new MyPlantFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_myplant, container, false);

        addPlant = (ImageButton) rootView.findViewById(R.id.addPlant);
        addPlant.setOnClickListener(this);

        //swipe 기능!!!!!!!!!!!!!!!!
        //내 식물 추가할 때마다 레이아웃 하나씩 증가
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler1);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);


        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);//adapter 지정

        getPlantListTask task = new getPlantListTask();
        Map<String, String> param = new HashMap<String, String>();
        SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String user_id = auto.getString("user_id", null);
        param.put("user_id", user_id);
        task.execute(param);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addPlant:
                ((MainActivity)getActivity()).replaceFragment(AddPlantFragment.newInstance());
                Log.d("fragment_change", "add Fragment in MainActivity");
                break;
        }
    }

    class getPlantListTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/selectPlant.do");
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
                    //식물 object 저장


                    SharedPreferences sp = getActivity().getSharedPreferences("plant_object", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();

                    JSONArray jarray = new JSONObject(s).getJSONArray("object");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject jObject = jarray.getJSONObject(i);
                        PlantList plant = new PlantList();

                        int num = jObject.getInt("num");
                        String name = jObject.getString("name");
                        String kind = jObject.getString("kind");
                        String water = jObject.getString("water");

                        //recycler view show
                        plant.setNum(num);
                        plant.setName(name);
                        plant.setKind(kind);
                        plant.setWater(water);

                        String number = Integer.toString(num);
                        editor.putString(number, jObject.toString()); // JSON으로 변환한 객체를 저장한다.
                        editor.commit(); //완료한다.

                        mAdapter.addItem(plant);
                    }
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "식물 목록 가져오기 성공", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}