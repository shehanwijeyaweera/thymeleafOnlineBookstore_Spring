package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Customer_orderItemsRepository extends JpaRepository<Customer_orderItems, Long> {
    @Query("SELECT i FROM Customer_orderItems i WHERE i.customer_orders.id = ?1")
    List<Customer_orderItems> getMyItems(long order_id);
}
