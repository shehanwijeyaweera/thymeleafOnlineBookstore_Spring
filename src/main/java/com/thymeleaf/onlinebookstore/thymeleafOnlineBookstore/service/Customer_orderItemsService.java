package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orderItems;

import java.util.List;

public interface Customer_orderItemsService {
    public void saveOrderItem(Customer_orderItems customer_orderItems);

    List<Customer_orderItems> getMyItems(long order_id);
}
