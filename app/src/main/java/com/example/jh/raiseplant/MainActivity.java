package com.example.jh.raiseplant;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.example.jh.raiseplant.MyPlant_Code.MyPlantFragment;
import com.example.jh.raiseplant.SelfCheck_Code.SelfCheckFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.example.jh.raiseplant.DrCheck_Code.DrCheckFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, KeyboardWatcher.OnKeyboardToggleListener{

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 메뉴에 들어갈 Fragment들
    private MyPlantFragment myPlantFragment = new MyPlantFragment();
    private SelfCheckFragment selfCheckFragment = new SelfCheckFragment();
    private DrCheckFragment dr_check_fragment = new DrCheckFragment();
    private MypageFragment mypage_fagment = new MypageFragment();
    private ARFragment arFragment = new ARFragment();


    //하단 메뉴 버튼 5개(4개 메뉴 + 가운데 버튼)
    public ImageView bt_ar;
    public BottomNavigationView curvedBottomNavigationView;

    //keyboard 제어
    private KeyboardWatcher keyboardWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AR 버튼 클릭
        bt_ar = (ImageView) findViewById(R.id.bt_ar);
        bt_ar.bringToFront();
        bt_ar.invalidate();
        bt_ar.setOnClickListener(this);

        curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        curvedBottomNavigationView.setItemIconTintList(null);


        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, myPlantFragment).commitAllowingStateLoss();


        //파일접근권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }//파일 접근 권한 얻기

        //sharedpreferenced 사용자 고유값 임의로 생성
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String user_id = auto.getString("user_id", null);
        Log.i("error_test", "userid " + user_id);

        if(user_id==null){
            SharedPreferences.Editor save_userid = auto.edit();
            save_userid.putString("user_id", Integer.toString((int)(Math.random() * 10000) + 1000));
            save_userid.commit();
        }

        //키보드 제어
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);

        SharedPreferences sp = getSharedPreferences("plant_object", Activity.MODE_PRIVATE);
        String strContact = sp.getString("18", null);

    } // end oncreate

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ar:
                new IntentIntegrator(this).initiateScan();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                //스캔 성공 시, arcore fragment로 이동
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, arFragment.newInstance(30, 30)).commitAllowingStateLoss();
                transaction.addToBackStack(null);
                /* 참고사이트
                https://stackoverflow.com/questions/52251070/how-to-display-png-image-in-arcore?noredirect=1&lq=1
                https://developers.google.com/ar/develop/java/sceneform/create-renderables
                https://codelabs.developers.google.com/codelabs/sceneform-intro/#4 5단계 테스트 진행중
                https://stackoverflow.com/questions/49818161/cant-install-arcore-on-emulator-for-android-studio
                */
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //키보드 올라왔을때
                curvedBottomNavigationView.setVisibility(View.GONE);
                bt_ar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onKeyboardClosed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //키보드 내려왔을때
                curvedBottomNavigationView.setVisibility(View.VISIBLE);
                bt_ar.setVisibility(View.VISIBLE);
            }
        });


    }
}
