# Chapter6 安卓存储与数据库操作 - Todolist APP

* 使用`Java`编写
* 主要有那些功能？
	* 1：通过`SharedPreferences`存储打开次数并显示在标题
	* 2：通过`SQLiteOpenHelper`存储打开次数并显示在标题
    	* `DB_VERSION`升级为`3`，在`onUpgrade()`升级操作中对数据库增加一个记录打开次数的`startups`表
	* 3：长按编辑修改某一条todolist的内容
    	* 并且会继承todolist原本的内容，包括文字和优先级的选择情况
    	* 通过`requestCode == REQUEST_CODE_UPDATE`判断更新事件
	* 4：更改了不同优先级的todolist的显示效果


> 在`MainActivity.java`通过外部重写`NoteListAdapter`中`item`的长按事件，这样在`MainActivity`中发起`intent`
> [参考1](https://blog.csdn.net/qq_42792745/article/details/81230897) [参考2](https://blog.csdn.net/qq_39714504/article/details/78165125)

By [Yiwei Yang](https://github.com/Smileglaze).
