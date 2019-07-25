package com.example.jh.raiseplant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 4개의 메뉴에 들어갈 Fragment들
    private MyPlantFragment myPlantFragment = new MyPlantFragment();
    private SelfCheckFragment selfCheckFragment = new SelfCheckFragment();
    private DrCheckFragment dr_check_fragment = new DrCheckFragment();
    private MypageFragment mypage_fagment = new MypageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomBottomNavigationView curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        curvedBottomNavigationView.setItemIconTintList(null);

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, myPlantFragment
        ).commitAllowingStateLoss();
    }

    private CustomBottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new CustomBottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.one:
                    //화면전환하기
                    transaction.replace(R.id.frame_layout, myPlantFragment).commitAllowingStateLoss();
                    transaction.addToBackStack(null);
                    break;
                case R.id.two:
                    transaction.replace(R.id.frame_layout, selfCheckFragment).commitAllowingStateLoss();
                    transaction.addToBackStack(null);
                    break;
                case R.id.four:
                    transaction.replace(R.id.frame_layout, dr_check_fragment).commitAllowingStateLoss();
                    transaction.addToBackStack(null);
                    break;
                case R.id.five:
                    transaction.replace(R.id.frame_layout, mypage_fagment).commitAllowingStateLoss();
                    transaction.addToBackStack(null);
                    break;
            }
            return true;
        }
    };

}
