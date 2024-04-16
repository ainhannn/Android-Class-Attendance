package com.example.classattendance.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Attendance {
    private int id;
    private Date time;
    private int classId;
    private int times;
    public List<AttendanceRecord> attendanceRecords;
    private String code;
    private int presentCount;
    private int lateCount;

    public Attendance() {}

    public Attendance(int id, Date time, int classId, int times, List<AttendanceRecord> attendanceRecords, String code, int presentCount, int lateCount) {
        this.id = id;
        this.time = time;
        this.classId = classId;
        this.times = times;
        this.attendanceRecords = attendanceRecords;
        this.code = code;
        this.presentCount = presentCount;
        this.lateCount = lateCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public AttendanceItem toAttendanceItem() {
        return new AttendanceItem(time.toString(), presentCount, lateCount);
    }
}
