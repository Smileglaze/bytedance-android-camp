package com.byted.camp.todolist.beans;

import android.graphics.Color;

import com.byted.camp.todolist.Constants;

public enum Priority {
    High(Constants.HIGH_VALUE, Color.parseColor(Constants.HIGH_COLOR)),
    Medium(Constants.MEDIUM_VALUE, Color.parseColor(Constants.MEDIUM_COLOR)),
    Low(Constants.LOW_VALUE, Color.parseColor(Constants.LOW_COLOR));

    public final int intValue;
    public final int color;

    Priority(int intValue, int color) {
        this.intValue = intValue;
        this.color = color;
    }

    public static Priority from(int intValue) {
        for (Priority priority : Priority.values()) {
            if (priority.intValue == intValue) {
                return priority;
            }
        }
        return Priority.Low; // default
    }
}
