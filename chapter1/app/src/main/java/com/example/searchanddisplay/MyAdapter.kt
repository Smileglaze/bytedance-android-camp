package com.example.searchanddisplay

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val contentList: List<String>) : RecyclerView.Adapter<MyViewHolder>() {

    // 创建一个成员Context变量，否则onBindViewHolder()无法访问主活动的context
    // 如果此类和主活动在同一个kt文件中，直接使用this即可获得context
    private lateinit var mContext: Context

    //下面这三个函数是创建时继承了RecyclerView.Adapter类会自动提示你需要实现的
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context // 获取整个列表组展示View的context
        // 在列表组展示View中使用item_layout.xml规定的样式进行填充，把整个View撑起来
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        // 获取有多少个item
        return contentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 给每个itemView绑定显示内容，注意要绑定到holder的对应成员中，这个成员是我们指定好的对应view
        val item = contentList[position] // 根据位置获取列表中对应item，这个Int来源是默认的，不用指定
        holder.text.text = item // 修改了TextView中的text属性
        // 给每个holder添加一个点击事件
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, Display::class.java)
            // 通过Intent传递数据到新的名叫Display的Activity中，"msg"是在Display.kt中指定的成员常量
            intent.putExtra("msg", item)
            mContext.startActivity(intent)
        }
    }
}