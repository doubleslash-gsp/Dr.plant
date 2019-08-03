package com.example.jh.raiseplant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MypageFragment extends Fragment implements View.OnClickListener {

    private TextView tv_name, tv_plantNum;
    private RelativeLayout layout_setting, layout_recommand, layout_privacy, layout_QA;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_mypage, container, false);



        SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String user_id = auto.getString("user_id", null);

        tv_name = (TextView) rootview.findViewById(R.id.mypage_name);
        tv_name.setText(user_id);

        SharedPreferences sp = getActivity().getSharedPreferences("plant_object", Activity.MODE_PRIVATE);

        Map<String,?> keys = sp.getAll();
        tv_plantNum = (TextView) rootview.findViewById(R.id.plantNum);
        tv_plantNum.setText(Integer.toString(keys.size()));

        layout_setting = (RelativeLayout) rootview.findViewById(R.id.layout_setting);
        layout_setting.setOnClickListener(this);

        layout_recommand = (RelativeLayout) rootview.findViewById(R.id.layout_Recommand);
        layout_recommand.setOnClickListener(this);

        layout_privacy = (RelativeLayout) rootview.findViewById(R.id.layout_privacy);
        layout_privacy.setOnClickListener(this);

        layout_QA = (RelativeLayout) rootview.findViewById(R.id.layout_Q_A);
        layout_QA.setOnClickListener(this);

        return rootview;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "There are no actions.", Toast.LENGTH_SHORT).show();
    }

}
