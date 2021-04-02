package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoContract.TodoNote;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.debug.DebugActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.byted.camp.todolist.db.TodoContract.STARTUPS_COUNT;
import static com.byted.camp.todolist.db.TodoContract.STARTUPS_NAME;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;
    private static final int REQUEST_CODE_UPDATE = 1003;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    private TodoDbHelper dbHelper;
    private SQLiteDatabase database;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        dbHelper = new TodoDbHelper(this);
        database = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notes = loadNotesFromDatabase();
        notesAdapter.refresh(notes);

        // 修改标题
        setTitleFromDatabase();
        // 添加监听
        notesAdapter.setOnItemLongClickListener(new NoteListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Bundle data=new Bundle();
                data.putString("text", notes.get(position).getContent());
                data.putBoolean("updateFlag", true);
                data.putLong("id", notes.get(position).id);
                data.putInt("priority", notes.get(position).getPriority().intValue);
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, REQUEST_CODE_UPDATE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        database = null;
        dbHelper.close();
        dbHelper = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_UPDATE)
                && resultCode == Activity.RESULT_OK) {
            notes = loadNotesFromDatabase();
            notesAdapter.refresh(notes);
        }
    }

    private void setTitleFromDatabase() {
        String title_sp = "SPtodo";
        String title_db = "DBtodo";

        // 从SharedPreferences读取计数
        int count_sp = -1;
        SharedPreferences sp = this.getSharedPreferences("todo", Context.MODE_PRIVATE);
        if (sp != null){
            count_sp = sp.getInt("count", -1);
        }
        SharedPreferences.Editor editor = sp.edit();
        if (count_sp == -1){
            editor.putInt("count", 1);
        } else {
            title_sp += count_sp;
            editor.putInt("count", count_sp + 1);
        }
        editor.apply();

        // 从数据库读取计数
        if (database == null) {
            setTitle(title_sp + " | " + title_db);
            return;
        }
        Cursor cursor = null;
        int count_db = -1;
        try {
            /*
            query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)方法各参数的含义：
                table：表名。相当于select语句from关键字后面的部分。如果是多表联合查询，可以用逗号将两个表名分开。
                columns：要查询出来的列名。相当于select语句select关键字后面的部分。
                selection：查询条件子句，相当于select语句where关键字后面的部分，在条件子句允许使用占位符“?”
                selectionArgs：对应于selection语句中占位符的值，值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。
                groupBy：相当于select语句group by关键字后面的部分
                having：相当于select语句having关键字后面的部分
                orderBy：相当于select语句order by关键字后面的部分，如：personid desc, age asc;
                limit：指定偏移量和获取的记录数，相当于select语句limit关键字后面的部分。
             */
            cursor = database.query(STARTUPS_NAME, null,
                    null, null,
                    null, null,
                    null,null);
            if (cursor.moveToNext()) {
                count_db = cursor.getInt(cursor.getColumnIndex(STARTUPS_COUNT));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        ContentValues values = new ContentValues();
        if (count_db == -1){
            // If there is no startup history.
            values.put(STARTUPS_COUNT, 1);
            database.insert(STARTUPS_NAME, null, values);
        } else {
            // If there is startup history.
            title_db += count_db;
            values.put(STARTUPS_COUNT, count_db + 1);
            database.update(STARTUPS_NAME, values, null,null);
        }

        setTitle(title_sp + " | " + title_db);
        return;
    }

    private List<Note> loadNotesFromDatabase() {
        if (database == null) {
            return Collections.emptyList();
        }
        List<Note> result = new LinkedList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(TodoNote.TABLE_NAME, null,
                    null, null,
                    null, null,
                    TodoNote.COLUMN_PRIORITY + " DESC");

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(TodoNote._ID));
                String content = cursor.getString(cursor.getColumnIndex(TodoNote.COLUMN_CONTENT));
                long dateMs = cursor.getLong(cursor.getColumnIndex(TodoNote.COLUMN_DATE));
                int intState = cursor.getInt(cursor.getColumnIndex(TodoNote.COLUMN_STATE));
                int intPriority = cursor.getInt(cursor.getColumnIndex(TodoNote.COLUMN_PRIORITY));

                Note note = new Note(id);
                note.setContent(content);
                note.setDate(new Date(dateMs));
                note.setState(State.from(intState));
                note.setPriority(Priority.from(intPriority));

                result.add(note);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    private void deleteNote(Note note) {
        if (database == null) {
            return;
        }
        int rows = database.delete(TodoNote.TABLE_NAME,
                TodoNote._ID + "=?",
                new String[]{String.valueOf(note.id)});
        if (rows > 0) {
            notes = loadNotesFromDatabase();
            notesAdapter.refresh(notes);
        }
    }

    private void updateNode(Note note) {
        if (database == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(TodoNote.COLUMN_STATE, note.getState().intValue);

        int rows = database.update(TodoNote.TABLE_NAME, values,
                TodoNote._ID + "=?",
                new String[]{String.valueOf(note.id)});
        if (rows > 0) {
            notes = loadNotesFromDatabase();
            notesAdapter.refresh(notes);
        }
    }

}
