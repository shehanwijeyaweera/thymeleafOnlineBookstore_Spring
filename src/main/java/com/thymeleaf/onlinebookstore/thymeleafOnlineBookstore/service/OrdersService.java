package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;


public interface OrdersService {
     Customer_orders create(Customer_orders orders);
     void saveOrder(Customer_orders orders);
}
