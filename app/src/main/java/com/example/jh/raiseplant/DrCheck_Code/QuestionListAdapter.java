package com.example.jh.raiseplant.DrCheck_Code;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jh.raiseplant.R;

import java.util.ArrayList;

public class QuestionListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<QuestionInfo> listViewItemList = new ArrayList<QuestionInfo>() ;

    // ListViewAdapter의 생성자
    public QuestionListAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_questionlist_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        LinearLayout ll_background = (LinearLayout) convertView.findViewById(R.id.ll_background);
        TextView tv_num = (TextView) convertView.findViewById(R.id.tv_num) ;
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title) ;
        TextView tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname) ;
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date) ;
        TextView tv_reply = (TextView) convertView.findViewById(R.id.tv_reply) ;
        ImageView iv_reply = (ImageView) convertView.findViewById(R.id.iv_reply) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        QuestionInfo listViewItem = listViewItemList.get(pos);

        // 아이템 내 각 위젯에 데이터 반영
        tv_num.setText((String.format("%02d", pos+1)));
        tv_title.setText(listViewItem.getTitle());
        tv_plantname.setText(listViewItem.getPlant_name());
        tv_date.setText(listViewItem.getDate());

        //답변여부에 따라 이미지뷰 보이게
        if(listViewItem.getReplay()>0){
            tv_reply.setText("답변완료");
            iv_reply.setVisibility(View.VISIBLE);
        }else{
            tv_reply.setText("답변대기");
            iv_reply.setVisibility(View.INVISIBLE);
        }

        //홀수, 짝수에 따라 배경 바꿔주기
        if(pos%2==0 || pos==0)
            ll_background.setBackgroundColor(convertView.getResources().getColor(R.color.one_background));
        else if(pos%2==1)
            ll_background.setBackgroundColor(convertView.getResources().getColor(R.color.two_background));
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String plant_name, String date, int reply, int board_num, int plant_num, String content, String user_id) {
        QuestionInfo item = new QuestionInfo();

        item.setTitle(title);
        item.setPlant_name(plant_name);
        item.setDate(date);
        item.setReplay(reply);
        item.setNum(board_num);
        item.setPlant_num(plant_num);
        item.setContent(content);
        item.setUser_id(user_id);

        listViewItemList.add(item);
    }
}
