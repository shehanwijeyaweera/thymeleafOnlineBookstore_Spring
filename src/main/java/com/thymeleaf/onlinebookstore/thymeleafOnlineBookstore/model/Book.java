package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private int ISBN;
    private String title;
    private String description;
    private BigDecimal price;
    private String publisher;
    private LocalDate pubdate;

    public Book() {
    }

    public Book(int ISBN, String title, String description, BigDecimal price, String publisher, LocalDate pubdate) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.price = price;
        this.publisher = publisher;
        this.pubdate = pubdate;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPubdate() {
        return pubdate;
    }

    public void setPubdate(LocalDate pubdate) {
        this.pubdate = pubdate;
    }

}
