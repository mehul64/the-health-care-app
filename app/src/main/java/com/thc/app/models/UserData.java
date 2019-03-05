package com.thc.app.models;

import java.util.HashMap;


public class UserData {

    private String name;
    private String mobile;
    private String email;
    private String password;
    private String weight;
    private String age;
    private String gender;
    private String height;

    private HashMap<String, OneDayData> activities = new HashMap<>();

    public UserData() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public HashMap<String, OneDayData> getActivities() {
        return activities;
    }

    public void setActivities(HashMap<String, OneDayData> activities) {
        this.activities = activities;
    }
}
