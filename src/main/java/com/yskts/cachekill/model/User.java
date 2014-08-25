package com.yskts.cachekill.model;

/**
 * Created by Nick on 8/24/2014.
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String description;

    public User() {
    }

    public User(int userId, String username, String password, String description) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
