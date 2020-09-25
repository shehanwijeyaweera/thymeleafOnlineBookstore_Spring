package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Requestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestBookRepository extends JpaRepository<Requestbook, Long> {
}
