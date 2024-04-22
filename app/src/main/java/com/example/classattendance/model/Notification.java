package com.example.classattendance.model;

import java.util.Date;

public class Notification {

    private int id;
    private Date time;
    private int classId;
    private int userId;
    private String userName;
    private String content;

    public Notification() {}
    public Notification(int id, Date time, int classId, int userId, String userName, String content) {
        this.id = id;
        this.time = time;
        this.classId = classId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
    }

    public Notification(Date time, String userName, String content) {
        this.time = time;
        this.userName = userName;
        this.content = content;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
