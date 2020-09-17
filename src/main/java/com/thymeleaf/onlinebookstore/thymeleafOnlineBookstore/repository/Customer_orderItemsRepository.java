package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Customer_orderItemsRepository extends JpaRepository<Customer_orderItems, Long> {
}
