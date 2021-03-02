package com.example.searchanddisplay

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val text: TextView = itemView.findViewById(R.id.textView)

}