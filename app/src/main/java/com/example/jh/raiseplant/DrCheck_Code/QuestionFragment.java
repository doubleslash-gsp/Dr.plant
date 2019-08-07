package com.example.jh.raiseplant.DrCheck_Code;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.raiseplant.Constants;
import com.example.jh.raiseplant.HttpClient;
import com.example.jh.raiseplant.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_FROM_ALBUM = 1; //return code
    private File tempFile; // 갤러리로부터 불러온 파일

    private ImageButton ib_showplant, ib_back;
    private Button bt_addImage, bt_save;
    private TextView tv_plantname, et_title;
    private EditText et_content;

    private int plant_num; //식물번호


    //fragment 반환
    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_question, container, false);

        et_title = (EditText) rootview.findViewById(R.id.et_title); //제목

        tv_plantname = (TextView) rootview.findViewById(R.id.tv_plantname); //context 메뉴 클릭마다 텍스트 변경
        ib_showplant = (ImageButton) rootview.findViewById(R.id.ib_showplant);
        ib_showplant.setOnClickListener(this); //short click context menu 등록
        registerForContextMenu(ib_showplant); //long click context menu 등록

        et_content = (EditText) rootview.findViewById(R.id.et_content); //bt_addImage 에 따라 이미지 삽입, 텍스트입력
        bt_addImage = (Button) rootview.findViewById(R.id.bt_addImage);
        bt_addImage.setOnClickListener(this); //사진 edittext에 삽입

        ib_back = (ImageButton) rootview.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this); //뒤로가기버튼

        bt_save = (Button) rootview.findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);

        return rootview;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Log.d("test", "onCreateContextMenu");
//        getMenuInflater().inflate(R.menu.main, menu);

        //모든 식물 정보 가져오기
        SharedPreferences sp = getActivity().getSharedPreferences("plant_object", Activity.MODE_PRIVATE);

        Map<String,?> keys = sp.getAll();
        JSONObject jObject = null;
        for(Map.Entry<String,?> entry : keys.entrySet()){
            try {
                jObject = new JSONObject(entry.getValue().toString());
                int getnum = jObject.getInt("num");
                String getname = jObject.getString("name");
                menu.add(0,getnum,100,getname);
            } catch (JSONException e) {
                e.printStackTrace();
            } //end try catch
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) { //context item 클릭시 textview text 바뀜
        tv_plantname.setText(item.getTitle());
        plant_num = item.getItemId();
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_showplant:
                getActivity().openContextMenu(v);
                break;
            case R.id.bt_addImage:
                goToAlbum();
                break;
            case R.id.bt_save:
                saveQuestion();
                break;
            case R.id.ib_back:
                getActivity().onBackPressed();
                break;
        }
    }

    //참고: https://black-jin0427.tistory.com/120
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM); // onActivityResult의 requestCode
    } //앨범 들어가기

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_ALBUM) {
            /* Uri 스키마를 content:/// 에서 file:/// 로  변경한다. */
            Cursor cursor = null;
            try {
                Uri photoUri = data.getData();

                String[] proj = { MediaStore.Images.Media.DATA };

                //assert photoUri != null;
                if(photoUri!=null){
                    cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                    if(cursor!=null){
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        cursor.moveToFirst();
                        tempFile = new File(cursor.getString(column_index));
                        setImage();
                    }
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
    }

    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        addToImageInEdittext(originalBm);

    }

    public void addToImageInEdittext(Bitmap bitmap){
        Drawable image = new BitmapDrawable(getActivity().getResources(), bitmap);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());

        int selectionCursor = et_content.getSelectionStart();
        et_content.getText().insert(selectionCursor, ".");
        selectionCursor = et_content.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(et_content.getText());
        //TODO: source 값으로 이미지 구분
        builder.setSpan(new ImageSpan(image, "test"), selectionCursor - ".".length(), selectionCursor,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_content.setText(builder);
        et_content.setSelection(selectionCursor);
    }

    public void saveQuestion(){

        if(et_title.equals("") || et_content.equals("") || tv_plantname.equals("식물 선택")){
            Toast.makeText(getActivity(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else{

            SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
            String user_id = auto.getString("user_id", null);

            SaveQuestionTask task = new SaveQuestionTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", user_id);
            params.put("title", et_title.getText().toString());

            /* TODO: edittext 이미지 저장 방법
            * https://stackoverflow.com/questions/2865452/is-it-possible-to-display-inline-images-from-html-in-an-android-textview
            */
            params.put("content", Html.toHtml(new SpannableStringBuilder(et_content.getText()), Html.FROM_HTML_MODE_LEGACY));
            params.put("plant_num", Integer.toString(plant_num));

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String getTime = sdf.format(date);
            params.put("date", getTime);

            task.execute(params);
        }
    }

    class SaveQuestionTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", Constants.server+ "/insertBoard.do");

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
                    Toast.makeText(getActivity(), "질문이 저장되었습니다.", Toast.LENGTH_SHORT).show();
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

