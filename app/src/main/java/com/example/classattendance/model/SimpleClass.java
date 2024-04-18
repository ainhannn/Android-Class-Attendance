package com.example.classattendance.model;

import java.util.Date;

public class SimpleClass {
    private int id;
    private String code;
    private Date time;
    private int teacherId;
    private String name;
    private String section;
    private String subject;
    private String room;
    private boolean isArchived;

    public SimpleClass(Class model) {
        this.id = model.getId();
        this.code = model.getCode();
        this.time = time;
        this.teacherId = teacherId;
        this.name = name;
        this.section = section;
        this.subject = subject;
        this.room = room;
        this.isArchived = isArchived;
    }

    public SimpleClass(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    public SimpleClass(int id, String code, Date time, int teacherId, String name, String section, String subject, String room, boolean isArchived) {
        this.id = id;
        this.code = code;
        this.time = time;
        this.teacherId = teacherId;
        this.name = name;
        this.section = section;
        this.subject = subject;
        this.room = room;
        this.isArchived = isArchived;
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
