package com.example.jh.raiseplant.DrCheck_Code;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jh.raiseplant.R;

import java.util.ArrayList;

public class ReplycheckListAdapter extends BaseAdapter {

    private AppCompatActivity appCompatActivity;
    private ArrayList<ReplyInfo> listViewItemList = new ArrayList<ReplyInfo>() ;

    public ReplycheckListAdapter() {

    }

    public ReplycheckListAdapter(AppCompatActivity appCompatActivity, ArrayList<ReplyInfo>  list) {
        super();
        this.appCompatActivity = appCompatActivity;
        this.listViewItemList = list;
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return listViewItemList.indexOf(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) appCompatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_replycheck_item, parent, false);
        }

        TextView content_one = (TextView) convertView.findViewById(R.id.tv_content_one);
        TextView content_two = (TextView) convertView.findViewById(R.id.tv_content_two);
        TextView writer = (TextView) convertView.findViewById(R.id.tv_writer);
        TextView datetime = (TextView) convertView.findViewById(R.id.tv_date_time);
        ReplyInfo listViewItem = listViewItemList.get(position);

        content_one.setText(listViewItem.getContent_one());
        content_two.setText(listViewItem.getContent_one());
        writer.setText("작성자: " + listViewItem.getWriter());
        datetime.setText(listViewItem.getDate_time());

        return convertView;
    }

}
