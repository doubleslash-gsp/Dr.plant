package com.example.jh.raiseplant.SelfCheck_Code;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jh.raiseplant.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    public class ListContents{
        String msg;
        String time;
        int type;
        ListContents(String _msg, String _time,int _type)
        {
            this.time = _time;
            this.msg = _msg; // 출력할 메세지
            this.type = _type; // 0 -> 상대방, 1 -> 사용자
        }
    }

    private ArrayList<ListContents> m_List;

    public CustomAdapter() {
        m_List = new ArrayList();
    }
    public void add(String _msg, String _time,int _type) {
        m_List.add(new ListContents(_msg, _time,_type)); // 리스트에 메세지에 대한 정보를 저장
    }
    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
    }
    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        CustomHolder holder = null;
        TextView text = null;
        TextView chatTime = null;
        LinearLayout chatMessage = null;
        LinearLayout layout = null;
        View viewRight = null;
        View viewLeft = null;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chatbot_item, parent, false);

            chatMessage = (LinearLayout) convertView.findViewById(R.id.chatMessage);
            layout = (LinearLayout) convertView.findViewById(R.id.layout);
            text = (TextView) convertView.findViewById(R.id.text);
            chatTime = (TextView) convertView.findViewById(R.id.time);
            viewRight = (View) convertView.findViewById(R.id.imageViewright);
            viewLeft = (View) convertView.findViewById(R.id.imageViewleft);

            holder = new CustomHolder();
            holder.chatMessage = chatMessage;
            holder.m_TextView = text;
            holder.time = chatTime;
            holder.layout = layout;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        }
        else {
            holder = (CustomHolder) convertView.getTag();
            chatMessage = holder.chatMessage;
            text = holder.m_TextView;
            chatTime = holder.time;
            layout = holder.layout;
            viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }

        text.setText(m_List.get(position).msg);
        chatTime.setText(m_List.get(position).time);
        if( m_List.get(position).type == 0 ) { // 0번일 경우 왼쪽에서 메세지 버블 생성
            text.setBackgroundResource(R.drawable.round_caht);
            chatTime.setVisibility(View.VISIBLE);
            chatMessage.setGravity(Gravity.RIGHT);
            layout.setGravity(Gravity.LEFT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.VISIBLE);

        }else if(m_List.get(position).type == 1){
            text.setBackgroundResource(R.drawable.round_caht_pink);
            chatMessage.setGravity(Gravity.LEFT);
            chatTime.setVisibility(View.VISIBLE);
            layout.setGravity(Gravity.RIGHT);
            viewRight.setVisibility(View.VISIBLE);
            viewLeft.setVisibility(View.GONE);

        }else if(m_List.get(position).type == 2){ // 1번일 경우 오른쪽에서 메세지 버블 생성
            text.setBackgroundColor(Color.WHITE);
            chatTime.setVisibility(View.GONE);
            layout.setGravity(Gravity.CENTER);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class CustomHolder { // 좌, 우측 view 객체에 대한 연산을 처리하기 위해 custom class 선언
        LinearLayout chatMessage;
        TextView m_TextView;
        TextView time;
        LinearLayout layout;
        View viewRight;
        View viewLeft;
    }
}