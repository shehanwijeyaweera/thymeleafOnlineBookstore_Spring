package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Set;

public interface BookService {
    List<Book> getAllBooks();
    Book findById(Long bookId);
    Long create(Book bookDetails);
    void update(Long bookId, Book bookDetails);
    void delete(Long bookId);
    Page<Book> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<Book> findAllSearch(String keyword);
}
