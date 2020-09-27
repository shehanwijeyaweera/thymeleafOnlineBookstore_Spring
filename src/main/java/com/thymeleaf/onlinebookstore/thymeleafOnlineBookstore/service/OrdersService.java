package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;

import java.util.List;


public interface OrdersService {
     Customer_orders create(Customer_orders orders);
     void saveOrder(Customer_orders orders);
     List<Customer_orders> getAllOrders();
     Customer_orders findById(Long orderID);

     List<Customer_orders> getCustomerOrders(long user_id);



}
