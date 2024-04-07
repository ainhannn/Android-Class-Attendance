package com.example.classattendance.model;

import java.util.Date;

public class SimpleUser {
    private int id;
    private String name;

    public SimpleUser() {}
    public SimpleUser(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
