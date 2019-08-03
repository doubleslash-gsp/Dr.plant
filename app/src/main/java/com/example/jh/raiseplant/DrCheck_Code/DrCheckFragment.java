package com.example.jh.raiseplant.DrCheck_Code;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

public class DrCheckFragment extends Fragment implements View.OnClickListener {

    private TextView tv_detail;
    private Button bt_question, bt_question_list;


    //fragment 반환
    public static DrCheckFragment newInstance() {
        return new DrCheckFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_drcheck, container, false);

        tv_detail = rootView.findViewById(R.id.tv_detail);
        tv_detail.setText(Html.fromHtml("의 전문가선생님들은," + "<br/>" +
                                                    "정확하고 빠른 진단을 통해" + "<br/>" +
                                                    "반려식물의 상태를 파악하고 처방합니다."));

        bt_question = rootView.findViewById(R.id.bt_question);
        bt_question.setOnClickListener(this);
        bt_question_list = rootView.findViewById(R.id.bt_question_list);
        bt_question_list.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_question:
                ((MainActivity)getActivity()).replaceFragment(QuestionFragment.newInstance());
                break;
            case R.id.bt_question_list:
                ((MainActivity)getActivity()).replaceFragment(QuestionListFragment.newInstance());
                break;

        }

    }
}
