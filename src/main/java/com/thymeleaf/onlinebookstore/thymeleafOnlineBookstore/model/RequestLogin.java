package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

public class RequestLogin {
    String Username;
    String Password;

    public RequestLogin(String username, String password) {
        Username = username;
        Password = password;
    }

    public RequestLogin() {
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
