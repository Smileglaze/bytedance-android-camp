package com.byted.camp.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class TodoDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todo.db";
    private static final int DB_VERSION = 3;

    public TodoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoContract.SQL_CREATE_NOTES);
        db.execSQL(TodoContract.SQL_CREATE_STARTUPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:
                    db.execSQL(TodoContract.SQL_ADD_PRIORITY_COLUMN);
                    break;
                case 2:
                    db.execSQL(TodoContract.SQL_CREATE_STARTUPS);
                    break;
            }
        }
    }
}
