package com.szh.enliarith.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szh.enliarith.R;

import java.util.List;

/**
 * Created by szh on 2017/4/1.
 */
public class CountRecyclerViewAdapter extends RecyclerView.Adapter<CountRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Integer> list;

    public CountRecyclerViewAdapter(Context mContext, List<Integer> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_count_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}
