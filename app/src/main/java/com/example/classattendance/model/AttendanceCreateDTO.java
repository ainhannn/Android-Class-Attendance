package com.example.classattendance.model;

import java.util.Date;

public class AttendanceCreateDTO {
    private int classId;
    private String location;
    private Date createTime;
    private int lateAfter;
    private int expiryAfter;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getLateAfter() {
        return lateAfter;
    }

    public void setLateAfter(int lateAfter) {
        this.lateAfter = lateAfter;
    }

    public int getExpiryAfter() {
        return expiryAfter;
    }

    public void setExpiryAfter(int expiryAfter) {
        this.expiryAfter = expiryAfter;
    }
}
