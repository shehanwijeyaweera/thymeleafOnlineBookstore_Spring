package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/homepage_cust")
    public String cust_homepage(){
        return "Customer_homepage";
    }

    @GetMapping("/about_cust")
    public String cust_aboutpage(){
        return "Customer_about";
    }

    @GetMapping("/cart_cust")
    public String cust_cartpage(){
        return "Cust_cart";
    }

    @GetMapping("/homepage_custold")
    public String custold_homepage(){
        return "customer_view-book";
    }

    @GetMapping("/")
    public String pageRedirection(Model model)
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Admin")))
        {
            return "index";
        }
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("User")))
        {
            return "Customer_homepage";
        }
        if(authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Storeworker")))
        {

        }

        return "index";
    }
}
