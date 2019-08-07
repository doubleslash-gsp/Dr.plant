package com.example.jh.raiseplant;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.example.jh.raiseplant.MyPlant_Code.PlantList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoReceiver extends BroadcastReceiver{

    Context context;
    Intent intent;
    StringBuilder content;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("error_test", "this is in InfoReceiver");
        this.context = context;
        this.intent = intent;
        SharedPreferences auto = context.getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String user_id = auto.getString("user_id", null);

        content = new StringBuilder("");
        getPlantInfoTask task = new getPlantInfoTask();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", user_id);
        task.execute(param);

    }

    class getPlantInfoTask extends AsyncTask<Map<String, String>, Integer, String> {

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

                    JSONArray jarray = new JSONObject(s).getJSONArray("object");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject jObject = jarray.getJSONObject(i);
                        PlantList plant = new PlantList();

                        content.append("식물: " + jObject.getString("name"));
                        content.append(", 습도: " + jObject.getInt("humidity"));
                        content.append(", 온도: " + jObject.getInt("temper"));
                        content.append(System.getProperty("line.separator"));
                    }

                    content.deleteCharAt(content.length()-1);

                    notification();

                }else{
                    Toast.makeText(context, "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public void notification(){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
            /**
             * 누가버전 이하 노티처리
             */
            Toast.makeText(context,"누가 이하",Toast.LENGTH_SHORT).show();

            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);



            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                    setLargeIcon(null)
                    .setSmallIcon(R.drawable.dr)
                    .setContentTitle("식물의 상태를 확인해주세요.")
                    .setWhen(System.currentTimeMillis()).
                            setShowWhen(true).
                            setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_MAX)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setContentText(content.toString())
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setFullScreenIntent(pendingIntent,true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,builder.build());

        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Toast.makeText(context,"오레오이상",Toast.LENGTH_SHORT).show();

            int importance = NotificationManager.IMPORTANCE_HIGH;
            String Noti_Channel_ID = "Noti";
            String Noti_Channel_Group_ID = "Noti_Group";

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(Noti_Channel_ID,Noti_Channel_Group_ID,importance);

            /**
             * 채널이 있는지 체크해서 없을경우 만들고 있으면 채널을 재사용합니다.
             */
            if(notificationManager.getNotificationChannel(Noti_Channel_ID) != null){
                //Toast.makeText(context,"채널이 이미 존재합니다.",Toast.LENGTH_SHORT).show();
            }
            else{
                //Toast.makeText(context,"채널이 없어서 만듭니다.",Toast.LENGTH_SHORT).show();
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,Noti_Channel_ID)
                    .setLargeIcon(null).setSmallIcon(R.drawable.dr)
                    .setWhen(System.currentTimeMillis()).setShowWhen(true).
                            setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentTitle("식물의 상태를 확인해주세요.")//내용
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setContentText(content.toString());

            notificationManager.notify(0,builder.build());
            Intent serviceIntent = new Intent(context, InfoReceiver.class);
            WakefulBroadcastReceiver.startWakefulService(context, serviceIntent);
            Log.i("error_test", "this is in inforeceiver");

        }
    }


}