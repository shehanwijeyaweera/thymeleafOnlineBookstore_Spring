package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;



    @Override
    public Customer_orders create(Customer_orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    public void saveOrder(Customer_orders orders) {
        this.ordersRepository.save(orders);
    }

    @Override
    public List<Customer_orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public Customer_orders findById(Long orderID) {
        Optional<Customer_orders> customer_ordersOptional = ordersRepository.findById(orderID);

        if(!customer_ordersOptional.isPresent()){
            throw new RuntimeException("order Not Found");
        }
        return customer_ordersOptional.get();
    }




}
