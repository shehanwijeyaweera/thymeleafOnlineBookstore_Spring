package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Customer_orders, Long> {
    @Query("SELECT i from Customer_orders i WHERE i.user.id = ?1 AND i.status <> 'Refunded' AND i.status <> 'Rejected'")
    List<Customer_orders> getCustomerOrders(long user_id);

    @Query("SELECT SUM(i.total) FROM Customer_orders i WHERE i.status <> 'Refunded'")
    double sumofsale();

    @Query("SELECT i from Customer_orders i WHERE i.id = ?1")
    Customer_orders findOrderbyId(Long order_id);

    @Query("SELECT i from Customer_orders i WHERE i.status = 'shipped'")
    List<Customer_orders> getAllshippedOrders();

    @Query("SELECT i from Customer_orders i WHERE i.status = 'Pending'")
    List<Customer_orders> getAllPendingOrders();

    @Query("SELECT i from Customer_orders  i WHERE i.name = 'New Order'")
    List<Customer_orders> getAllNewOrders();


}
