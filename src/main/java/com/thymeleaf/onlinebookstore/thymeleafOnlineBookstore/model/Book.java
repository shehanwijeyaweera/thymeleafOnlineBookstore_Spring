package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private String logo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pubdate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_category",
            joinColumns = { @JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_author",
            joinColumns = { @JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")}
    )
    private Set<Author> authors = new HashSet<>();

    public Book() {
    }

    public Book(int ISBN, String title, String description, BigDecimal price, String publisher, String logo, LocalDate pubdate, Set<Category> categories, Set<Author> authors) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.price = price;
        this.publisher = publisher;
        this.logo = logo;
        this.pubdate = pubdate;
        this.categories = categories;
        this.authors = authors;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Transient
    public String getLogoImagePath(){
        if (logo == null || bookId == null) return null;

        return "/logos/" + bookId + "/" + logo;
    }
}
