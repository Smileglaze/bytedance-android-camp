package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract.TodoNote;
import com.byted.camp.todolist.db.TodoDbHelper;

import static android.view.View.GONE;


public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private Button updateBtn;
    private RadioGroup radioGroup;
//    private AppCompatRadioButton lowRadio;

    private TodoDbHelper dbHelper;
    private SQLiteDatabase database;

    private String text = "";
    private Boolean updateFlag = false;
    private long id = -1;
    private int priorityValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();


        dbHelper = new TodoDbHelper(this);
        database = dbHelper.getWritableDatabase();

        editText = findViewById(R.id.edit_text);

        if (data != null){
            text = data.getString("text","");
            id = data.getLong("id",0);
            updateFlag = data.getBoolean("updateFlag",false);
            priorityValue = data.getInt("priority", 0);
            editText.setText(text);
        }

        editText.setFocusable(true);
        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }
        radioGroup = findViewById(R.id.radio_group);
        setSelectedPriority(priorityValue);

        addBtn = findViewById(R.id.btn_add);
        updateBtn = findViewById(R.id.btn_update);
        if (updateFlag){
            addBtn.setVisibility(GONE);
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence content = editText.getText();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(NoteActivity.this,
                                "No content to update", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean succeed = updateNote2Database(content.toString().trim(),
                            getSelectedPriority(), id);
                    if (succeed) {
                        Toast.makeText(NoteActivity.this,
                                "Note updated", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                    } else {
                        Toast.makeText(NoteActivity.this,
                                "Error", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
        } else {
            updateBtn.setVisibility(GONE);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence content = editText.getText();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(NoteActivity.this,
                                "No content to add", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean succeed = saveNote2Database(content.toString().trim(),
                            getSelectedPriority());
                    if (succeed) {
                        Toast.makeText(NoteActivity.this,
                                "Note added", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                    } else {
                        Toast.makeText(NoteActivity.this,
                                "Error", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        database = null;
        dbHelper.close();
        dbHelper = null;
    }

    private boolean saveNote2Database(String content, Priority priority) {
        if (database == null || TextUtils.isEmpty(content)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(TodoNote.COLUMN_CONTENT, content);
        values.put(TodoNote.COLUMN_STATE, State.TODO.intValue);
        values.put(TodoNote.COLUMN_DATE, System.currentTimeMillis());
        values.put(TodoNote.COLUMN_PRIORITY, priority.intValue);
        long rowId = database.insert(TodoNote.TABLE_NAME, null, values);
        return rowId != -1;
    }

    private boolean updateNote2Database(String content, Priority priority, long id) {
        if (database == null || TextUtils.isEmpty(content)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(TodoNote.COLUMN_CONTENT, content);
        values.put(TodoNote.COLUMN_STATE, State.TODO.intValue);
        values.put(TodoNote.COLUMN_DATE, System.currentTimeMillis());
        values.put(TodoNote.COLUMN_PRIORITY, priority.intValue);
        // TODO 更新
        long rowId = database.update(TodoNote.TABLE_NAME, values,TodoNote._ID + "=?", new String[]{String.valueOf(id)});
        return rowId != -1;
    }

    private Priority getSelectedPriority() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.btn_high:
                return Priority.High;
            case R.id.btn_medium:
                return Priority.Medium;
            default:
                return Priority.Low;
        }
    }

    private void setSelectedPriority(int value) {
        AppCompatRadioButton btnView;
        switch (value) {
            case Constants.HIGH_VALUE:
                btnView = findViewById(R.id.btn_high);
                break;
            case Constants.MEDIUM_VALUE:
                btnView = findViewById(R.id.btn_medium);
                break;
            default:
                btnView = findViewById(R.id.btn_low);
        }
        btnView.setChecked(true);
    }
}
