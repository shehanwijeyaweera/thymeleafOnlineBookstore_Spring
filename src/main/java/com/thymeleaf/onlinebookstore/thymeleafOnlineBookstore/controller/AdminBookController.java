package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/adminbook/")
public class AdminBookController {

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


    //display list of Authors
    @GetMapping("authors")
    public String ViewAllAuthors(Model model){
        model.addAttribute("listAuthors", authorService.getAllAuthors());

        return "admin_viewAllAuthors";
    }

    @GetMapping("showNewAuthorForm")
    public String showNewAuthorForm(Model model){
        //creates model attribute to bind form data
        Author author = new Author();
        model.addAttribute("author", author);
        return "admin_addAuthor";
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
        return "admin_editAuthor";
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
        return "admin_viewAllCategories";
    }

    @GetMapping("showNewCategoryForm")
    public String showNewCategoryForm(Model model){
        Category category = new Category();
        model.addAttribute("category", category);

        return "admin_addCategory";
    }

    @PostMapping("saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category){
        categoryService.saveCategory(category);
        return "redirect:/adminbook/category";
    }

    @GetMapping("showFormCategoryUpdate/{category_id}")
    public String showFormCategoryUpdate(@PathVariable(value = "category_id") long category_id, Model model){
        //get categories from service
        Category category = categoryService.getCategoryById(category_id);

        //set category as a model attribute to populate the form
        model.addAttribute("category", category);
        return "admin_editCategory";
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
       return findPaginated(1, "title", "asc", model);
    }

    //show a single book details
    @RequestMapping("show/{book_id}")
    public String showSingleBook(@PathVariable("book_id")long book_id, Model model){
        Book book = bookService.findById(book_id);
        model.addAttribute("book", book);
        return "admin_singleBookView";
    }

    @RequestMapping("book/create")
    public String newBookForm(Model model){
        model.addAttribute("book", new Book());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);
        return "admin_addbook";
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

    @GetMapping("page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,@RequestParam("sortField") String sortField ,@RequestParam("sortDir") String sortDir, Model model){
        int pageSize = 5;

        model.addAttribute("listCategories", categoryService.getAllCategories());
        model.addAttribute("listAuthors", authorService.getAllAuthors());

        Page<Book> page = bookService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Book> listBooks = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listBooks", listBooks);

        return "view-book";
    }

    @RequestMapping("search")
    public String ViewSearchindex(Model model, @Param("keyword") String keyword){
        model.addAttribute("listCategories", categoryService.getAllCategories());
        model.addAttribute("listAuthors", authorService.getAllAuthors());
        List<Book> listbooks = bookService.findAllSearch(keyword);
        model.addAttribute("listBooks", listbooks);

        return "view-book";
    }

    @GetMapping("deleteBook/{book_Id}")
    public String deleteBook(@PathVariable(value = "book_Id")long book_Id){
        //call delete book method
        this.bookService.delete(book_Id);

        return "redirect:/adminbook/adminViewallbooks";
    }

    //display list of orders
    @GetMapping("order")
    public String ViewAllOrders(Model model){
        List<Customer_orders> customer_orders = ordersRepository.getAllNewOrders();
        model.addAttribute("listOrders", customer_orders);
        return "admin_viewAllOrders";
    }

    //display single book order details with order items
    @RequestMapping("order/{order_id}")
    public String showSingleOrder(@PathVariable("order_id")long order_id, Model model){
        Customer_orders customer_orders = ordersService.findById(order_id);
        List<Customer_orderItems> bookList= customer_orderItemsService.getMyItems(order_id);
        model.addAttribute("customer_order", customer_orders);
        model.addAttribute("items",bookList);
        return "admin_viewSingleOrderDetails";
    }

    @GetMapping("/newadminhomepage")
    public String admin_homepage(Model model){
        model.addAttribute("listOrders", ordersService.getAllOrders().size());
        model.addAttribute("allcustomers", userRepository.findAll().size());
        model.addAttribute("allbooks", bookRepository.findAll().size());
        model.addAttribute("totalsales", ordersRepository.sumofsale());
        return "admin_homepage";
    }

    @GetMapping("/adminViewallbooks")
    public String admin_allBooks(Model model){
        model.addAttribute("listbooks", bookRepository.findAll());
        return "admin_ViewAllBooks";
    }

    //admin add new user
    @GetMapping("/adminaddUser")
    public String admin_adduser(Model model){
        model.addAttribute("user", new UserRegistrationDto());
        return "admin_addUser";
    }

    //save new user data
    @PostMapping("/adminaddUser/save")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto){
        userService.save(registrationDto);
        return "redirect:/users/list";
    }

    //show user update form
    @GetMapping("userdetails/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "admin_editUser";
    }

    //save updated user details
    @PostMapping("userdetails/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "admin_editUser";
        }

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "admin_viewAllUsers";
    }

    @GetMapping("refund")
    private String ViewAllrefundRequest(Model model){
        model.addAttribute("listrefunds", refundRepository.getAllNotResponedRefundReq());
        return "admin_viewAllRefundRequests";
    }

    @RequestMapping("refundAccept/{order_id}/response/{refund_id}")
    private String acceptRefund(@PathVariable("order_id")Long order_id, @PathVariable("refund_id") Long refund_id){
        Customer_orders customer_orders = ordersRepository.findOrderbyId(order_id);
        Refund refund = refundRepository.findRefundById(refund_id);
        refund.setRespond("responded");
        customer_orders.setStatus("Refunded");
        refundRepository.save(refund);
        ordersRepository.save(customer_orders);
        return "redirect:/adminbook/refund";
    }

    @RequestMapping("refundReject/{order_id}/response/{refund_id}")
    private String rejectRefund(@PathVariable("order_id")Long order_id, @PathVariable("refund_id")Long refund_id){
        Customer_orders customer_orders = ordersRepository.findOrderbyId(order_id);
        Refund refund = refundRepository.findRefundById(refund_id);
        customer_orders.setStatus("Rejected");
        refund.setRespond("responded");
        ordersRepository.save(customer_orders);
        refundRepository.save(refund);
        return "redirect:/adminbook/refund";
    }

    @GetMapping("refund/accepted")
    private String showRefundedReqs(Model model){
        List<Refund> refund = refundRepository.getAllRefundedReqs();
        model.addAttribute("Refundedreqs", refund);
        return "admin_showAllRefundedReqs";
    }

    @GetMapping("refund/rejected")
    private String showRejectedRefundReqs(Model model){
        List<Refund> refund = refundRepository.getAllRejectedRefundReqs();
        model.addAttribute("RejectedReqs", refund);
        return "admin_showAllRejectedReqs";
    }

    @GetMapping("requestBook")
    private String showALlbookRequests(Model model){
        model.addAttribute("listofbookreq", requestBookRepository.findAll());
        return "admin_showAllBookRequests";
    }

    @GetMapping("shippedOrders")
    private String viewAllshippedOrders(Model model){
        List<Customer_orders> customer_orders = ordersRepository.getAllshippedOrders();
        model.addAttribute("listshippedOrders", customer_orders);
        return "admin_viewAllShippedOrders";
    }

    @GetMapping("pendingOrders")
    private String viewAllPendingOrders(Model model){
        List<Customer_orders> customer_orders = ordersRepository.getAllPendingOrders();
        model.addAttribute("listpendingOrders", customer_orders);
        return "admin_viewAllPendingOrders";
    }
}
