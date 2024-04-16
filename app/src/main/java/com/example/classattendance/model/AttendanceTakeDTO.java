package com.example.classattendance.model;

import java.util.Date;

public class AttendanceTakeDTO {
    private String location;
    private Date time;
    private String code;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

