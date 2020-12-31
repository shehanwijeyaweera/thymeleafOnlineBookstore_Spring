package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Requestbook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long request_id;
    private String bookName;
    private int quantity;
    private Date reqDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Requestbook() {
    }

    public Requestbook(String bookName, int quantity, Date reqDate, User user) {
        this.bookName = bookName;
        this.quantity = quantity;
        this.reqDate = reqDate;
        this.user = user;
    }

    public Long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(Long request_id) {
        this.request_id = request_id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
