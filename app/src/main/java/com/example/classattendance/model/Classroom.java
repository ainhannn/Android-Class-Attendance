package com.example.classattendance.model;

public class Classroom {
    private String name;
    private String subject;

    public Classroom(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }
}
