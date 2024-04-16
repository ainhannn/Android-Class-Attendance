package com.example.classattendance.model;

import java.util.List;

public class AttendanceItem {
    private String time;
    private int presentCount;
    private int lateCount;

    public List<AttendanceStudent> attendaceStudent;

    public AttendanceItem(String time, int presentCount, int lateCount) {
        this.time = time;
        this.presentCount = presentCount;
        this.lateCount = lateCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }
}