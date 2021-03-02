package com.example.searchanddisplay

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollingActivity : AppCompatActivity() {

    private val list = mutableListOf<String>()
    //private lateinit var myRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }//这个图标被我禁止显示了，所以这个功能并没有什么用

        for (i in 1..100) list.add("这里是第 $i 行") //这个是kotlin独特的字符串模板，用$套一个变量，可以引用其值

        //添加RecyclerView的样式和数据更新方法
        val myRecycler = findViewById<RecyclerView>(R.id.recyclerView)
        myRecycler.layoutManager = LinearLayoutManager(this)
        myRecycler.adapter = MyAdapter(list)

        //规定SearchView的侦听事件
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyWord: String): Boolean {
                //当提交了输入时的操作
                return false
            }

            override fun onQueryTextChange(keyWord: String): Boolean {
                // 当修改了输入时的操作，根据关键字过滤列表，让Adapter填入新列表
                // 但这应该并不是最好的方式，常规的方式是在你修改了装进Adapter中的数据后，
                // 使用myRecycler.adapter.notifyDataSetChanged()，适用于数据热更新的场景
                val filterList = filter(keyWord)
                myRecycler.adapter = MyAdapter(filterList)
                return false
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filter(keyWord: String): List<String> {
        // 过滤原本的列表，返回一个新的列表
        val filterList = mutableListOf<String>()

        for (l in list) {
            if (l.contains(keyWord)) filterList.add(l)
        }
        return filterList
    }
}