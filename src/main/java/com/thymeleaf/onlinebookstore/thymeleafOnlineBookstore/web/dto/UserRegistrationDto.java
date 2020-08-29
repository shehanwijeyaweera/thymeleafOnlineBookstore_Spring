package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.web.dto;

public class UserRegistrationDto {
    private String user_fName;
    private String user_lName;
    private String user_email;
    private String user_address;
    private int user_phoneNo;
    private String userRole;
    private String username;
    private String password;

    public UserRegistrationDto(String user_fName, String user_lName, String user_email, String user_address, int user_phoneNo, String userRole, String username, String password) {
        this.user_fName = user_fName;
        this.user_lName = user_lName;
        this.user_email = user_email;
        this.user_address = user_address;
        this.user_phoneNo = user_phoneNo;
        this.userRole = userRole;
        this.username = username;
        this.password = password;
    }

    public UserRegistrationDto() {
    }

    public String getUser_fName() {
        return user_fName;
    }

    public void setUser_fName(String user_fName) {
        this.user_fName = user_fName;
    }

    public String getUser_lName() {
        return user_lName;
    }

    public void setUser_lName(String user_lName) {
        this.user_lName = user_lName;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public int getUser_phoneNo() {
        return user_phoneNo;
    }

    public void setUser_phoneNo(int user_phoneNo) {
        this.user_phoneNo = user_phoneNo;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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
}
