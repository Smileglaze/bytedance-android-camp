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
        var msg=intent.getStringExtra("msg")
        findViewById<TextView>(R.id.textDisplay).text =msg
        //findViewById<ConstraintLayout>(R.id.constraintDisplay).accessibilityPaneTitle =msg
    }
}