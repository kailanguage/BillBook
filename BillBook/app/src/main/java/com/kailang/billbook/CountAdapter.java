package com.kailang.billbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.MyViewHolder> {
    private List<Count> allCount = new ArrayList<>();
    private CountViewModel countViewModel;

    private static CountClickListener clickListener;

    public CountAdapter(CountViewModel countViewModel) {
        this.countViewModel = countViewModel;
    }

    public void setAllCount(List<Count> allCount) {
        this.allCount = allCount;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.count_card,parent,false);

        final MyViewHolder holder = new MyViewHolder(itemView);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到编辑count
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Count count = allCount.get(position);
        holder.tv_describe.setText(count.getDescribe());
        holder.tv_money.setText(count.getMoney()+"");
    }

    @Override
    public int getItemCount() {
        return allCount.size();
    }

    //自定义ViewHolder:内部类，static 防止内存泄露
    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_describe,tv_money;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_describe=itemView.findViewById(R.id.tv_describe);
            tv_money=itemView.findViewById(R.id.tv_money);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }

    public void setOnItemClickListener(CountClickListener clickListener){
        this.clickListener=clickListener;
    }

    //点击事件接口
    public interface CountClickListener {
        void onItemClick(int position, View v);
        //void onItemLongClick(int position, View v);
    }
}
