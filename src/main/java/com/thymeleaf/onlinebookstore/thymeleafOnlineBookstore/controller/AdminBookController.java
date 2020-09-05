package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Author;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Category;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.AuthorService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adminbook/")
public class AdminBookController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

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

    @GetMapping("showFormAuthorUpdate/{author_id}")
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

    //display list of Categories
    @GetMapping("category")
    public String ViewAllCategories(Model model){
        model.addAttribute("listCategories", categoryService.getAllCategories());
        return "view-category";
    }

    @GetMapping("showNewCategoryForm")
    public String showNewCategoryForm(Model model){
        Category category = new Category();
        model.addAttribute("category", category);

        return "add_category";
    }

    @PostMapping("saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category){
        categoryService.saveCategory(category);
        return "redirect:/adminbook/category?success";
    }

    @GetMapping("showFormCategoryUpdate/{category_id}")
    public String showFormCategoryUpdate(@PathVariable(value = "category_id") long category_id, Model model){
        //get categories from service
        Category category = categoryService.getCategoryById(category_id);

        //set category as a model attribute to populate the form
        model.addAttribute("category", category);
        return "edit_category";
    }

    @GetMapping("deleteCategory/{category_id}")
    public String deleteCategory(@PathVariable(value = "category_id")long category_id){
        //call delete category method
        this.categoryService.deleteCategoryById(category_id);

        return "redirect:/adminbook/category?successdelete";
    }
}
