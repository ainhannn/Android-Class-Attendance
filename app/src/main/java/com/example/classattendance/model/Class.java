package com.example.classattendance.model;

import java.util.Date;
import java.util.List;

public class Class {
    private int id;
    private String code;
    private Date time;
    private String name;
    private String section;
    private String subject;
    private String room;
    private SimpleUser teacher;
    private List<Notification> notifications;
    private List<SimpleUser> members;

    public Class() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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