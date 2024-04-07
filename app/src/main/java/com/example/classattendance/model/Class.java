package com.example.classattendance.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Class {
    @SerializedName("Id")
    private int id;
    @SerializedName("Time")
    private Date time;
    @SerializedName("Name")
    private String name;
    @SerializedName("Section")
    private String section;
    @SerializedName("Subject")
    private String subject;
    @SerializedName("Room")
    private String room;
    @SerializedName("Teacher")
    private SimpleUser teacher;
    @SerializedName("Notifications")
    public List<Notification> notifications;
    @SerializedName("Members")
    public List<SimpleUser> members;

    public Class() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public SimpleUser getTeacher() {
        return teacher;
    }

    public void setTeacher(SimpleUser teacher) {
        this.teacher = teacher;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<SimpleUser> getMembers() {
        return members;
    }

    public void setMembers(List<SimpleUser> members) {
        this.members = members;
    }
}