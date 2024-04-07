package com.example.classattendance.model;

import com.google.gson.annotations.SerializedName;

public class ClassDTO {
    @SerializedName("Name")
    private String name;
    @SerializedName("Section")
    private String section;
    @SerializedName("Subject")
    private String subject;
    @SerializedName("Room")
    private String room;

    public ClassDTO() {}

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
}
