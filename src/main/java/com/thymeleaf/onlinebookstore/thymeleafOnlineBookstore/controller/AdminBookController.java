package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Author;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Category;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.AuthorService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.BookService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/adminbook/")
public class AdminBookController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

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

    //display list of books
    @GetMapping("book")
    public String ViewAllBooks(Model model){
        model.addAttribute("listBooks", bookService.getAllBooks());
        model.addAttribute("listCategories", categoryService.getAllCategories());
        model.addAttribute("listAuthors", authorService.getAllAuthors());
        return "view-book";
    }

    //show a single book details
    @RequestMapping("show/{book_id}")
    public String showSingleBook(@PathVariable("book_id")long book_id, Model model){
        Book book = bookService.findById(book_id);
        model.addAttribute("book", book);
        return "showSingleBookById";
    }

    @RequestMapping("book/create")
    public String newBookForm(Model model){
        model.addAttribute("book", new Book());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);
        return "new-book";
    }

    @PostMapping("book/create")
    public String saveNewBook(@ModelAttribute("book") Book book, @RequestParam("fileImage")MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        book.setLogo(fileName);
        long id = bookService.create(book);
        String uploadDir = "./logos/" + id ;

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(fileName);
            System.out.println(filePath.toFile().getAbsolutePath());

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e){
            throw new IOException("could not save uploaded file: "+ fileName);
        }
        return "redirect:/adminbook/book";
    }
}
