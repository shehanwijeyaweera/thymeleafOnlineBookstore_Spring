package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
}
