package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long refund_Id;

    private String reason;
    private Date reqDate;
    private String Respond;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordersid")
    private Customer_orders customer_orders;

    public Refund() {
    }

    public Refund(String reason, Date reqDate, String respond, User user, Customer_orders customer_orders) {
        this.reason = reason;
        this.reqDate = reqDate;
        Respond = respond;
        this.user = user;
        this.customer_orders = customer_orders;
    }

    public Long getRefund_Id() {
        return refund_Id;
    }

    public void setRefund_Id(Long refund_Id) {
        this.refund_Id = refund_Id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Customer_orders getCustomer_orders() {
        return customer_orders;
    }

    public void setCustomer_orders(Customer_orders customer_orders) {
        this.customer_orders = customer_orders;
    }

    public String getRespond() {
        return Respond;
    }

    public void setRespond(String respond) {
        Respond = respond;
    }
}
