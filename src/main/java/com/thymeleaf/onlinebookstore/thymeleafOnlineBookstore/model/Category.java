package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long category_id;
    private String category_name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_fid", referencedColumnName = "category_id")
    private List<Book> books = new ArrayList<>();

    public Category() {
    }

    public Category(String category_name, List<Book> books) {
        this.category_name = category_name;
        this.books = books;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
