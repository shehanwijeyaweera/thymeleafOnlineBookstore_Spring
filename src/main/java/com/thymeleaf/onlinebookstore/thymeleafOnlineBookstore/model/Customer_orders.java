package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Customer_orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer_orders")
    private List<Customer_orderItems> customer_orderItems;

    public Customer_orders() {
    }

    public Customer_orders(User user, String name, Date datecreation, String status, List<Customer_orderItems> customer_orderItems) {
        this.user = user;
        this.name = name;
        this.datecreation = datecreation;
        this.status = status;
        this.customer_orderItems = customer_orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
