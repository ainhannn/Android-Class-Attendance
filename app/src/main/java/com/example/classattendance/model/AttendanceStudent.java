package com.example.classattendance.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AttendanceStudent {
    private String time;
    private String name;
    private boolean present;

    public AttendanceStudent(String time, String name, boolean present) {
        this.time = time;
        this.name = name;
        this.present = present;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
        return "Name: " + name + ", Attendance Time: " + sdf.format(time) + ", Present: " + present;
    }
}
