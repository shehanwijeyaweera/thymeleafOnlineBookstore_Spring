package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orderItems;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.Customer_orderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class Customer_orderItemsServiceImpl implements Customer_orderItemsService {

    @Autowired
    private Customer_orderItemsRepository customer_orderItemsRepository;

    @Override
    public void saveOrderItem(Customer_orderItems customer_orderItems) {
        this.customer_orderItemsRepository.save(customer_orderItems);
    }
}
