package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> userRole;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "complaint_fid", referencedColumnName = "id")
    private List<Complaint> complaints = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Customer_orders> orders = new HashSet<Customer_orders>(0);

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Refund> refunds;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Requestbook> requestbooks;

    private boolean enabled;
    private String verificationCode;

    public User() {
    }

    public User(String username, String password, String user_fName, String user_lName, String user_email, int user_phoneNo, String user_address, Collection<Role> userRole, boolean enabled, String verificationCode) {
        this.username = username;
        this.password = password;
        this.user_fName = user_fName;
        this.user_lName = user_lName;
        this.user_email = user_email;
        this.user_phoneNo = user_phoneNo;
        this.user_address = user_address;
        this.userRole = userRole;
        this.enabled = enabled;
        this.verificationCode = verificationCode;
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

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public Set<Customer_orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Customer_orders> orders) {
        this.orders = orders;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
    }

    public List<Requestbook> getRequestbooks() {
        return requestbooks;
    }

    public void setRequestbooks(List<Requestbook> requestbooks) {
        this.requestbooks = requestbooks;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
