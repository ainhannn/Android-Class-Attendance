package com.example.classattendance.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttendanceRecord {
    private int attendanceId;
    private int userId;
    private Date time;
    private int status;
    private String userName;

    public AttendanceRecord() {}

    public AttendanceRecord(int attendanceId, int userId, Date time, int status, String userName) {
        this.attendanceId = attendanceId;
        this.userId = userId;
        this.time = time;
        this.status = status;
        this.userName = userName;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
        return "Name: " + userName + ", Attendance Time: " + sdf.format(time) + ", Present: " + (status == 1);
    }
}