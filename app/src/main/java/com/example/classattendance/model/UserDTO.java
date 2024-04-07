package com.example.classattendance.model;

import com.google.gson.annotations.SerializedName;

public class UserDTO {

    @SerializedName("Name")
    private String name;
    @SerializedName("UID")
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
