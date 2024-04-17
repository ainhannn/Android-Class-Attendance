package com.example.classattendance.model;

import java.util.List;

public class AttendanceItem {
    private String time;
    private int presentCount;
    private int lateCount;

    public List<AttendanceStudent> attendanceStudentList;

    public AttendanceItem(String time, int presentCount, int lateCount, List<AttendanceStudent> attendanceStudentList) {
        this.time = time;
        this.presentCount = presentCount;
        this.lateCount = lateCount;
        this.attendanceStudentList = attendanceStudentList;
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

    public List<AttendanceStudent> getAttendanceStudentList() {
        return attendanceStudentList;
    }

    public void setAttendanceStudent(List<AttendanceStudent> attendanceStudent) {
        this.attendanceStudentList = attendanceStudent;
    }
}