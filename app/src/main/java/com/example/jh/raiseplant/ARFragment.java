package com.example.jh.raiseplant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.sceneform.ux.ArFragment;

import java.util.List;

public class ARFragment extends Fragment {

    private ArFragment fragment;

    private ViewGroup mainLayout;
    private ImageView imageView;
    private TextView tv_humidity, tv_temper;
    private LinearLayout linearLayout;
    private int xDelta, yDelta;
    private int humidity, temperature;
    private StringBuffer src_value = new StringBuffer();




    //fragment 반환
    public static ARFragment newInstance(int humidity, int temperature) {
        ARFragment ar_fragment = new ARFragment();
        Bundle params = new Bundle();
        params.putInt("humidity", humidity);
        params.putInt("temperature", temperature);
        ar_fragment.setArguments(params);
        return ar_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            humidity = getArguments().getInt("humidity");
            temperature = getArguments().getInt("temperature");
            src_value.append(CheckValue(humidity));
            src_value.append("_");
            src_value.append(CheckValue(temperature));
            Log.i("error_test", src_value.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_ar, container, false);

        fragment = ((ArFragment)getChildFragmentManager().findFragmentById(R.id.ux_fragment));
        //fragment.getDiscoveryController().hide();
        //fragment.getDiscoveryController().setInstructionView(null);

        mainLayout = (RelativeLayout) rootview.findViewById(R.id.content_layout); //parent layout
        imageView = (ImageView) rootview.findViewById(R.id.image);
        int resId = Drawable_ID(src_value.toString()); //이미지 id
        imageView.setImageResource(resId);

        tv_humidity = (TextView) rootview.findViewById(R.id.tv_humidity);
        tv_humidity.setText("습도: " + humidity);
        tv_temper = (TextView) rootview.findViewById(R.id.tv_temper);
        tv_temper.setText("온도: " + temperature);

        linearLayout = (LinearLayout) rootview.findViewById(R.id.linearlayout);
        linearLayout.setOnTouchListener(onTouchListener());

        return rootview;
    }

    public int Drawable_ID(String imageName) {
        return getResources().
                getIdentifier(imageName.split("\\.")[0], "drawable", getContext().getPackageName());

    }

    public String CheckValue(int value){
        if(value<20)
            return "one";
        else if (value>=20 && value<40)
            return "two";
        else if (value >=40 && value <60)
            return  "three";
        else if (value >=60 && value <80)
            return "four";
        else
            return "five";
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();
                        Log.i("error_test", "i'm in action_down");
                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        Log.i("error_test", "i'm in action_move");
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                mainLayout.invalidate();
                return true;
            }
        };
    }

}
