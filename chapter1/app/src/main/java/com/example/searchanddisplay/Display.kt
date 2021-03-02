package com.example.searchanddisplay

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class Display : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        // 这里指定了要获取的常量的名字，通过名字传递不同的消息
        val msg=intent.getStringExtra("msg")
        // 修改新活动的TextView，显示msg内容
        findViewById<TextView>(R.id.textDisplay).text =msg
    }
}