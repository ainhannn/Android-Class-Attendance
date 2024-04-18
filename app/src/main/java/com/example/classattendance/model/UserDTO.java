package com.example.classattendance.model;

public class UserDTO {

    private String name;
    private String uid;

    public UserDTO() {
    }

    public UserDTO(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
