package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long author_id;
    private String author_name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_fid", referencedColumnName = "author_id")
    private List<Book> books = new ArrayList<>();

    public Author() {
    }

    public Author(String author_name, List<Book> books) {
        this.author_name = author_name;
        this.books = books;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
