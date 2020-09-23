package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Customer_orders, Long> {
    @Query("SELECT i from Customer_orders i WHERE i.user.id = ?1")
    List<Customer_orders> getCustomerOrders(long user_id);

    @Query("SELECT SUM(i.total) FROM Customer_orders i")
    double sumofsale();
}
