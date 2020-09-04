package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminbook/")
public class AdminBookController {

    @Autowired
    private AuthorService authorService;

    //display list of Authors
    @GetMapping("authors")
    public String ViewAllAuthors(Model model){
        model.addAttribute("listAuthors", authorService.getAllAuthors());

        return "view-authors";
    }
}
