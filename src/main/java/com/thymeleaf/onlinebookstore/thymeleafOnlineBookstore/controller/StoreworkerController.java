package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/storeworker/")
public class StoreworkerController {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private Customer_orderItemsService customer_orderItemsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private RequestBookRepository requestBookRepository;

    @Autowired
    private Customer_orderItemsRepository customer_orderItemsRepository;

    //display store worker homepage
    @GetMapping("homepage")
    public String ViewAllAuthors(Model model){
        model.addAttribute("pendingorders", ordersRepository.getAllPendingOrders().size());
        model.addAttribute("shippedorders", ordersRepository.getAllshippedOrders().size());
        model.addAttribute("allbooks", customer_orderItemsRepository.sumofbooksold());
        model.addAttribute("totalsales", requestBookRepository.findAll().size());

        return "Storeworker_homepage";
    }

    @GetMapping("shipped/{order_id}")
    public String markAsShipped(@PathVariable("order_id")Long order_id){
        Customer_orders customer_orders = ordersRepository.findOrderbyId(order_id);
        customer_orders.setName("shipped");
        customer_orders.setStatus("shipped");
        ordersRepository.save(customer_orders);
        return "redirect:/adminbook/order";
    }
}
