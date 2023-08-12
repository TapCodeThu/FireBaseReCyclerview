package com.example.pushdata01;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int id;
    private String name;
    private String email;
    private Job job;

    public User() {
    }

    public User(int id, String name, String email, Job job) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.job = job;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", job=" + job +
                '}';
    }
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("name",name);
        result.put("email",email);
        return result;
    }
}
