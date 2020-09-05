package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Author;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("showNewAuthorForm")
    public String showNewAuthorForm(Model model){
        //creates model attribute to bind form data
        Author author = new Author();
        model.addAttribute("author", author);
        return "add_author";
    }

    @PostMapping("saveAuthor")
    public String saveAuthor(@ModelAttribute("author") Author author){
        //save author to database
        authorService.saveAuthor(author);
        return "redirect:/adminbook/authors?success";
    }

    @GetMapping("showFormUpdate/{author_id}")
    public String showAuthorEditForm(@PathVariable(value = "author_id") long author_id, Model model){
        //get Author from the service
        Author author = authorService.getAuthorById(author_id);

        //set author as a model attribute to populate the form to update info
        model.addAttribute("author", author);
        return "edit_author";
    }

    @GetMapping("deleteAuthor/{author_id}")
    public String deleteAuthor(@PathVariable (value = "author_id") long author_id){

        //call delete employee method
        this.authorService.deleteAuthorById(author_id);

        return "redirect:/adminbook/authors?successdelete";
    }
}
