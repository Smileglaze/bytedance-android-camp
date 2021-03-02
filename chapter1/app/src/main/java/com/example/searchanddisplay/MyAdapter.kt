package com.example.searchanddisplay

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(val contentList: List<String>) : RecyclerView.Adapter<MyViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = contentList[position]
        holder.text.text = item
        holder.itemView.setOnClickListener {
            var intent = Intent(mContext, Display::class.java)
            intent.putExtra("msg", item)
            mContext.startActivity(intent)
            //mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext as Activity?).toBundle())
        }
    }

//    public fun setFilter(filterList: List<String?>, keyWord :String) {
//        keyWord = filterList
//        notifyDataSetChanged()
//    }
}