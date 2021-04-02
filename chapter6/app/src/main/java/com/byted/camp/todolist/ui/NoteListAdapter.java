package com.byted.camp.todolist.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.byted.camp.todolist.MainActivity;
import com.byted.camp.todolist.NoteActivity;
import com.byted.camp.todolist.NoteOperator;
import com.byted.camp.todolist.R;
import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.db.TodoContract;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private final NoteOperator operator;
    private final List<Note> notes = new ArrayList<>();
    private Context mContext;

    public NoteListAdapter(NoteOperator operator) {
        this.operator = operator;
    }

    // 暴露接口用于外部修改长按函数
    private OnItemLongClickListener mOnItemLongClickListener;
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    public void refresh(List<Note> newNotes) {
        notes.clear();
        if (newNotes != null) {
            notes.addAll(newNotes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView, operator);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int pos) {
        holder.bind(notes.get(pos));
        // Do not treat position as fixed; only use immediately and call holder.getAdapterPosition() to look it up later.
        // Otherwise use @SuppressLint("RecyclerView") annotation before 'final int pos'.
        final int position = holder.getAdapterPosition();
        // 给itemView绑定事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // onItemLongClick函数通过外部修改并传递intent
                // 参考https://blog.csdn.net/qq_42792745/article/details/81230897
                // 参考https://blog.csdn.net/qq_39714504/article/details/78165125
                mOnItemLongClickListener.onItemLongClick(view, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
