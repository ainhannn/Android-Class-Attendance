package com.example.classattendance.model;

import java.util.Date;

public class AttendanceCreateDTO {
    private int classId;
    private String location;
    private Date createTime;
    private Date lateTime;
    private Date expiryTime;

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

    public Date getLateTime() {
        return lateTime;
    }

    public void setLateTime(Date lateTime) {
        this.lateTime = lateTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }
}
