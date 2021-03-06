package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
    void saveAuthor(Author author);
    Author getAuthorById(long author_id);
    void deleteAuthorById(long author_id);
}
