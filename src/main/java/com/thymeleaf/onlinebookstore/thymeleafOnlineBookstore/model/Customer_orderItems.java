package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Customer_orderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordersid")
    private Customer_orders customer_orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookid")
    private Book book;
    private BigDecimal price;
    private int quantity;

    public Customer_orderItems() {
    }

    public Customer_orderItems(Customer_orders customer_orders, Book book, BigDecimal price, int quantity) {
        this.customer_orders = customer_orders;
        this.book = book;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer_orders getCustomer_orders() {
        return customer_orders;
    }

    public void setCustomer_orders(Customer_orders customer_orders) {
        this.customer_orders = customer_orders;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
