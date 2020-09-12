package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT p FROM Book p WHERE p.title LIKE %?1%"
            + " OR p.publisher LIKE %?1%"
            + " OR p.description LIKE %?1%")
    public List<Book> findAllSearch(String keyword);
}
