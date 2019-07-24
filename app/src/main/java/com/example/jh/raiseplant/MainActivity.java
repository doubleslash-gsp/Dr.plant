package com.example.jh.raiseplant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        CustomBottomNavigationView curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.navigation);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private CustomBottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new CustomBottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.one:
                    mTextMessage.setText(R.string.title_home);
                    //화면전환하기
                    return true;
                case R.id.two:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.four:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.five:
                    mTextMessage.setText(R.string.title_home);
                    return true;
            }
            return false;
        }
    };

}
