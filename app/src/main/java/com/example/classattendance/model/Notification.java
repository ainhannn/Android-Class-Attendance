package com.example.classattendance.model;

import java.util.Date;

public class Notification {

    private int id;
    private Date time;
    private int classId;
    private int userId;
    private String username;
    private String content;

    public Notification() {}
    public Notification(int id, Date time, int classId, int userId, String username, String content) {
        this.id = id;
        this.time = time;
        this.classId = classId;
        this.userId = userId;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
