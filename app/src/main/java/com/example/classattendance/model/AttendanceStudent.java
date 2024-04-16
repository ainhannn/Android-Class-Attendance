package com.example.classattendance.model;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceStudent {
    private Date time;
    private String name;
    private Boolean present;

    public AttendanceStudent(String time, String name, Boolean present) {
        this.time = formatDate(time);
        this.name = name;
        this.present = present;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Boolean isPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    private Date formatDate(String timeString) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
        try {
            return sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
