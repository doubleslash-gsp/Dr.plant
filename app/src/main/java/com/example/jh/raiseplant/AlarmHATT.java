package com.example.jh.raiseplant;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.jh.raiseplant.MyPlant_Code.PlantList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AlarmHATT {

    private Context context;

    public AlarmHATT(Context context) {
        this.context=context;


    }

    public void Alarm() {


        //notification alarm

        Intent intent = new Intent(context, InfoReceiver.class); //알림 발생시 InfoReveiver로 전송
//        intent.putExtra("content", content.toString());

        PendingIntent sender = PendingIntent.getBroadcast(context,
                                                0,
                                                            intent,
                                                            0);

        //알람시간 calendar에 set해주기
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 7);
        calendar.set(Calendar.SECOND, 50);

        if(calendar.before(Calendar.getInstance())){ // if it's in the past, increment
            calendar.add(Calendar.DATE, 1);
        }



        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE); //알림 행위 등록
        //알람 예약
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        Log.i("error_test", "this is in alarmhatt");

    }

}