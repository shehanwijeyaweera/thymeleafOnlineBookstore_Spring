package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String user_fName;
    private String user_lName;
    private String user_email;
    private int user_phoneNo;
    private String user_address;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> userRole;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "complaint_fid", referencedColumnName = "id")
    private List<Complaint> complaints = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Customer_orders> orders = new HashSet<Customer_orders>(0);

    public User() {
    }

    public User(String username, String password, String user_fName, String user_lName, String user_email, int user_phoneNo, String user_address, Collection<Role> userRole) {
        this.username = username;
        this.password = password;
        this.user_fName = user_fName;
        this.user_lName = user_lName;
        this.user_email = user_email;
        this.user_phoneNo = user_phoneNo;
        this.user_address = user_address;
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getUser_phoneNo() {
        return user_phoneNo;
    }

    public void setUser_phoneNo(int user_phoneNo) {
        this.user_phoneNo = user_phoneNo;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public Collection<Role> getUserRole() {
        return userRole;
    }

    public void setUserRole(Collection<Role> userRole) {
        this.userRole = userRole;
    }
}
