package com.example.chapter3.homework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        public ViewHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.text_view);
        }

    }

    public MyAdapter(List<String> contentList) {
        this.list = contentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.content.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}