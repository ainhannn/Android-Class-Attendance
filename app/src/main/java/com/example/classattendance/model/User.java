package com.example.classattendance.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class User {
    private int id;
    private Date time;
    @SerializedName("UID")
    private String uid;
    private String name;
    private List<SimpleClass> createdClasses;
    private List<SimpleClass> joinedClasses;

    public User() {}

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimpleClass> getCreatedClasses() {
        return createdClasses;
    }

    public void setCreatedClasses(List<SimpleClass> createdClasses) {
        this.createdClasses = createdClasses;
    }

    public List<SimpleClass> getJoinedClasses() {
        return joinedClasses;
    }

    public void setJoinedClasses(List<SimpleClass> joinedClasses) {
        this.joinedClasses = joinedClasses;
    }
}
