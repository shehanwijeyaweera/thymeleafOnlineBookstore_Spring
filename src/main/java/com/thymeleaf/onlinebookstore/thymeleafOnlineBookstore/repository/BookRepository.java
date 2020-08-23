package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
