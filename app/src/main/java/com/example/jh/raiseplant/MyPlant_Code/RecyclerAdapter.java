package com.example.jh.raiseplant.MyPlant_Code;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.raiseplant.MainActivity;
import com.example.jh.raiseplant.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<PlantList> listData = new ArrayList<PlantList>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_myplantlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    // RecyclerView의 총 개수
    @Override
    public int getItemCount() {
        return listData.size();
    }

    // 외부에서 item을 추가시킬 함수
    void addItem(PlantList data) {
        listData.add(data);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView plantname, plantkind, plantwater;
        private ImageView plantpic;
        private ImageButton btnplantdiary;

        ItemViewHolder(View itemView) {
            super(itemView);

            plantname = itemView.findViewById(R.id.plantname);
            plantkind = itemView.findViewById(R.id.plantkind);
            plantwater = itemView.findViewById(R.id.plantwater);
            plantpic = itemView.findViewById(R.id.plantpic);
            btnplantdiary = itemView.findViewById(R.id.btnplantdiary);

        }

        void onBind(PlantList data) {
            plantname.setText(data.getName());
            plantkind.setText(data.getKind());
            plantwater.setText(data.getWater());
//            plantpic.setImageResource(data.getPic());
            btnplantdiary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DiaryPlantFragment diaryPlantFragment = new DiaryPlantFragment();
                    int getnum = data.getNum();
                    ((MainActivity)itemView.getContext()).replaceFragment(diaryPlantFragment.newInstance(getnum));
                    Log.i("error_test", "adapter number: " + getnum);

                }
            });
        }
    }

}