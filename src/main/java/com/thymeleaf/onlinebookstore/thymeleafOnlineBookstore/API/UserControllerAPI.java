package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.API;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.JSON.Checkout;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.web.dto.UserRegistrationDto;
import net.bytebuddy.utility.RandomString;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class UserControllerAPI {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private RequestBookRepository requestBookRepository;

    @Autowired
    private Customer_orderItemsService customer_orderItemsService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthorService authorService;






    @GetMapping("/books")
    public List<Book> getallbooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/book/{id}")
    public Book getbookdetails(@PathVariable(value = "id") Long bookid) {
        return bookService.findById(bookid);
    }

    @GetMapping("/login/{username}/{password}")
    public JSONObject login(@PathVariable(value = "username") String username, @PathVariable(value = "password") String password) {
        JSONObject obj = new JSONObject();
        User user = new User();
        user = userRepository.findByUsername(username);
        String pass = user.getPassword();
        if (user != null) {
            if (userService.passwordencode(password, pass)) {

                obj.put("user", user);
                obj.put("Response", "Correct");
                obj.put("Role", user.getUserRole());
                return obj;
            } else {

                obj.put("username", user.getUsername());
                obj.put("userRole", user.getUserRole());
                obj.put("Response", "Wrong");
                return obj;
            }
        } else {
            obj.put("username", "Error");
            obj.put("userRole", "Error");
            obj.put("Response", "Error");
            return obj;
        }

    }

    @GetMapping("/admindashboard")
    public JSONObject dashboardDetails() {
        JSONObject obj = new JSONObject();
        obj.put("listOrders", ordersService.getAllOrders().size());
        obj.put("allcustomers", userRepository.findAll().size());
        obj.put("allbooks", bookRepository.findAll().size());
        obj.put("totalsales", ordersRepository.sumofsale());
        obj.put("totalrefunds", refundRepository.getAllNotResponedRefundReq().size());
        obj.put("bookRequest", requestBookRepository.findAll().size());

        return obj;
    }

    @GetMapping("/custorders")
    public List<Customer_orders> getAllCustomerOrders() {
        List<Customer_orders> customer_orders = ordersRepository.getAllNewOrders();
        return customer_orders;
    }

    @GetMapping("/custpendingorders")
    public List<Customer_orders> getAllPendingOrders() {
        List<Customer_orders> customer_orders = ordersRepository.getAllPendingOrders();
        return customer_orders;
    }

    @GetMapping("/custshippedorders")
    public List<Customer_orders> getAllShippedOrders() {
        List<Customer_orders> customer_orders = ordersRepository.getAllshippedOrders();
        return customer_orders;
    }

    @GetMapping("/customerorders/{userid}")
    public List<Customer_orders> getCustomerPastOrders(@PathVariable(value = "userid") Long userid) {
        List<Customer_orders> customer_orders = ordersRepository.getCustomerOrders(userid);
        return customer_orders;
    }

    @PostMapping("/checkout/{username}/{total}")
    public JSONObject checkout(@PathVariable(value = "username") String username, @PathVariable(value = "total") double total, @RequestBody List<CartItem> cartItem) {

        String username1 = username;
        double total1 = total;
        List<CartItem> cartItems = new ArrayList<>();
        cartItems = cartItem;

        User user = userRepository.findByUsername(username);

        //order creation
        Customer_orders orders = new Customer_orders();
        orders.setUser(user);
        orders.setDatecreation(new Date());
        orders.setName("New Order");
        orders.setStatus("Pending");
        orders.setTotal(total);

        ordersService.saveOrder(orders);

        Long orderId = orders.getId();

        //add order items
        for (CartItem cartItem1 : cartItem) {
            Customer_orderItems customer_orderItems = new Customer_orderItems();
            customer_orderItems.setBook(cartItem1.getBook());
            customer_orderItems.setCustomer_orders(orders);
            customer_orderItems.setPrice(cartItem1.getBook().getPrice());
            customer_orderItems.setQuantity(cartItem1.getQuantity());

            customer_orderItemsService.saveOrderItem(customer_orderItems);
        }

        JSONObject obj = new JSONObject();
        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @PostMapping("/newUser/register")
    public JSONObject registerUser(@RequestBody UserRegistrationDto registrationDto) throws UnsupportedEncodingException, MessagingException {
        JSONObject obj = new JSONObject();
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto = registrationDto;
        String username = registrationDto.getUsername();

        if (userRepository.findByUsername(username) != null) {
            obj.put("user", registrationDto);
            obj.put("Response", "Username Already taken");
        } else {
            registrationDto.setUserRole("User");
            registrationDto.setEnabled(false);

            String randomcode = RandomString.make(64);

            registrationDto.setVerificationCode(randomcode);

            userService.save(registrationDto);

            sendVerificationEmail(registrationDto);

            obj.put("user", registrationDto);
            obj.put("Response", "Correct");
        }

        return obj;
    }

    public void sendVerificationEmail(UserRegistrationDto registrationDto) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "OnlineBookStore team";
        String mailContent = "<p>Dear " + registrationDto.getUser_fName() + ",</p>";
        mailContent += "<p>Please Click the link below to verify to activate your account: </p>";
        mailContent += "<a>http://192.168.8.120:8080/registration/verify/"+ registrationDto.getVerificationCode() +"/"+registrationDto.getUsername()+"</a>";
        mailContent += "<p>Thank you <br> The OnlineBookStore Team </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("onlinebookstorelk@gmail.com", senderName);
        helper.setTo(registrationDto.getUser_email());
        helper.setSubject(subject);

        helper.setText(mailContent, true);

        mailSender.send(message);
    }

    @GetMapping("/search/{keyword}")
    public List<Book> getSearchResults(@PathVariable(value = "keyword") String keyword) {
        List<Book> bookList = bookService.findAllSearch(keyword);
        return bookList;
    }

    @PostMapping("/bookRequest")
    public JSONObject bookRequestResponse(@RequestBody Requestbook requestbook){
        JSONObject obj = new JSONObject();
        Requestbook requestbook1 = new Requestbook();
        requestbook1 = requestbook;

        String username;
        username = requestbook1.getUser().getUsername();

        User user = userRepository.findByUsername(username);

        requestbook1.setReqDate(new Date());
        requestbook1.setUser(user);

        requestBookRepository.save(requestbook1);

        obj.put("user", "User");
        obj.put("Response", "Correct");
        return obj;
    }

    @GetMapping("/user/refundReq/{user_id}")
    public List<Refund> getAllCustomerRefundRequests(@PathVariable(value = "user_id")Long id){
        List<Refund> refunds = refundRepository.getCustomerResponededreq(id);
        return refunds;
    }

    @PostMapping("user/refundReq/{order_id}/{user_id}")
    public JSONObject RefundRequestResponse(@RequestBody Refund refund,@PathVariable(value = "order_id")Long id,@PathVariable(value = "user_id")Long user_id){
        JSONObject obj = new JSONObject();
        Refund refund1 = new Refund();
        refund1 = refund;

        //get user
        User user = userRepository.getUserbyId(user_id);

        Customer_orders customer_orders = ordersRepository.findOrderbyId(id);

        refund1.setUser(user);
        refund1.setReqDate(new Date());
        refund1.setCustomer_orders(customer_orders);

        refundRepository.save(refund1);

        obj.put("user", "User");
        obj.put("Response", "Correct");
        return obj;
    }

    @GetMapping("/user/getprofileInfo/{user_id}")
    public User getUserprofileInfo(@PathVariable(value = "user_id")Long id){
        User user = userRepository.getUserbyId(id);
        return user;
    }

    @PostMapping("/user/updateprofile/{user_id}")
    public JSONObject updateProfileInfo(@PathVariable(value = "user_id")Long id, @RequestBody User user){
        JSONObject obj = new JSONObject();

        //get current user data
        User currentUserDate = userRepository.getUserbyId(id);

        //update data with sent data
        if(user.getUser_fName()!=null){
            currentUserDate.setUser_fName(user.getUser_fName());
        }
        if(user.getUser_lName()!=null){
            currentUserDate.setUser_lName(user.getUser_lName());
        }
        if(user.getUser_email()!=null){
            currentUserDate.setUser_email(user.getUser_email());
        }
            currentUserDate.setUser_phoneNo(user.getUser_phoneNo());

        if(user.getUser_address()!=null){
            currentUserDate.setUser_address(user.getUser_address());
        }

        userRepository.save(currentUserDate);

        String test = "test";

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @PostMapping("/admin/updatebook/save/{bookId}")
    public JSONObject updatebookDetails(@PathVariable(value = "bookId")Long bookId, @RequestBody Book book){
        JSONObject obj = new JSONObject();

        //get current book data
        Book currentBookData = bookRepository.findbyBookId(bookId);

        //update data with sent data
        if(book.getTitle()!=null){
            currentBookData.setTitle(book.getTitle());
        }
        if(book.getPubdate()!=null){
            currentBookData.setPubdate(book.getPubdate());
        }
        if(book.getDescription()!=null){
            currentBookData.setDescription(book.getDescription());
        }
        if(book.getPublisher()!=null){
            currentBookData.setPublisher(book.getPublisher());
        }
        if(book.getPrice()!=null){
            currentBookData.setPrice(book.getPrice());
        }

        bookRepository.save(currentBookData);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("/admin/categories")
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @GetMapping("/admin/authors")
    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    @PostMapping("/admin/updateCategory/{categoryid}")
    public JSONObject updateCategoryDetails(@PathVariable(value = "categoryid")Long categoryID, @RequestBody Category category){
        JSONObject obj = new JSONObject();

        //get current category data
        Category currentCategoryData = categoryRepository.findByCategoryID(categoryID);

        //update data with sent data
        if(category.getCategory_name()!=null){
            currentCategoryData.setCategory_name(category.getCategory_name());
        }

        categoryRepository.save(currentCategoryData);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @PostMapping("/admin/updateAuthor/{authorid}")
    public JSONObject updateAuthorDetails(@PathVariable(value = "authorid")Long authorID, @RequestBody Author author){
        JSONObject obj = new JSONObject();

        //get current author Data
        Author currentAuthorData = authorRepository.findByAuthorID(authorID);

        //update with sent data
        if(author.getAuthor_name()!=null){
            currentAuthorData.setAuthor_name(author.getAuthor_name());
        }

        authorRepository.save(currentAuthorData);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("/admin/deleteCategory/{categoryid}")
    public JSONObject deleteCategory(@PathVariable(value = "categoryid")Long categoryID){
        JSONObject obj = new JSONObject();

        //delete category
        categoryRepository.deleteById(categoryID);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("/admin/deleteAuthor/{authorID}")
    public JSONObject deleteAuthor(@PathVariable(value = "authorID")Long authorID){
        JSONObject obj = new JSONObject();

        //delete author
        authorRepository.deleteById(authorID);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @PostMapping("/admin/addnewCategory/save")
    public JSONObject addNewCategory(@RequestBody Category category){
        JSONObject obj = new JSONObject();

        if(category.getCategory_name()!=null) {
            categoryService.saveCategory(category);
        }

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @PostMapping("/admin/addnewAuthor/save")
    public JSONObject addNewAuthor(@RequestBody Author author){
        JSONObject obj = new JSONObject();

        if(author.getAuthor_name()!=null){
            authorService.saveAuthor(author);
        }

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("/admin/getAllNonresponededRefundreq")
    public List<Refund> getAllNonResponededRefundReq(){
        return refundRepository.getAllNotResponedRefundReq();
    }

    @GetMapping("/admin/getAllRefundedRefunds")
    public List<Refund> getAllRefundedRefunds(){
        return refundRepository.getAllRefundedReqs();
    }

    @GetMapping("/admin/getAllRejectedRefunds")
    public List<Refund> getAllRejectedRefunds(){
        return refundRepository.getAllRejectedRefundReqs();
    }

    @GetMapping("/admin/acceptRequest/{refundId}")
    public JSONObject acceptRequest(@PathVariable(value = "refundId")Long refundID){
        JSONObject obj = new JSONObject();
        Refund refund = refundRepository.findRefundById(refundID);
        Customer_orders customer_orders = ordersRepository.findOrderbyId(refund.getCustomer_orders().getId());

        refund.setRespond("responded");
        customer_orders.setStatus("Refunded");

        refundRepository.save(refund);
        ordersRepository.save(customer_orders);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("admin/rejectRequest/{refundId}")
    public JSONObject rejectRequest(@PathVariable(value = "refundId")Long refundID){
        JSONObject obj = new JSONObject();

        Refund refund = refundRepository.findRefundById(refundID);
        Customer_orders customer_orders = ordersRepository.findOrderbyId(refund.getCustomer_orders().getId());

        customer_orders.setStatus("Rejected");
        refund.setRespond("responded");

        refundRepository.save(refund);
        ordersRepository.save(customer_orders);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("/admin/getAllBookRequests")
    public List<Requestbook> getAllBookRequests(){
        return requestBookRepository.findAll();
    }

    @PostMapping("/admin/savebook/{categoryID}/{authorID}")
    public JSONObject saveNewBook( @RequestBody Book book, @PathVariable(value = "categoryID") Long categoryid, @PathVariable(value = "authorID")Long  authorid) throws IOException {
        JSONObject obj = new JSONObject();

        Author newAuthor = new Author();
        Category newCategory = new Category();

        newAuthor = authorRepository.findByAuthorID(authorid);
        newCategory = categoryRepository.findByCategoryID(categoryid);

        //add author for book
        book.getAuthors().add(newAuthor);

        //add category for book
        book.getCategories().add(newCategory);

        //add book to category and author
        newCategory.getBooks().add(book);
        newAuthor.getBooks().add(book);

        long id = bookService.create(book);

        obj.put("user", "User");
        obj.put("Response", id);

        return obj;
    }


    @PostMapping("/admin/savebookImage/{bookId}")
    public JSONObject saveBookImage(@PathVariable("bookId")Long bookID, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        JSONObject obj = new JSONObject();

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        Book savedBook = new Book();
        savedBook = getbookdetails(bookID);

        savedBook.setLogo(fileName);

        bookRepository.save(savedBook);

        long id = savedBook.getBookId();

        String uploadDir = "./logos/" + id;

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


        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("/admin/addBook/getSpinnerData")
    public JSONObject sendSpinnerData(){
        JSONObject obj = new JSONObject();

        obj.put("Category", categoryRepository.findAll());
        obj.put("Author", authorRepository.findAll());

        return obj;
    }

    @GetMapping("/admin/getAllusers")
    public List<User> getAllUsers(){
       return userRepository.findAll();
    }

    @GetMapping("/admin/deleteUser/{userID}")
    public JSONObject deleteUser(@PathVariable(value = "userID") Long userID){
        JSONObject obj = new JSONObject();

        User getUser = userRepository.getUserbyId(userID);

        String test = "test";

        userRepository.delete(getUser);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }

    @GetMapping("/admin/markOrderAsShipped/{orderID}")
    public JSONObject markOrderAsShipped(@PathVariable(value = "orderID")Long orderID){
        JSONObject obj = new JSONObject();

        Customer_orders customer_orders = new Customer_orders();
        customer_orders = ordersRepository.findOrderbyId(orderID);

        customer_orders.setName("shipped");
        customer_orders.setStatus("shipped");
        ordersRepository.save(customer_orders);

        obj.put("user", "User");
        obj.put("Response", "Correct");

        return obj;
    }
}
