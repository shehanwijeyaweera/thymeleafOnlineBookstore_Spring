package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.API;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.JSON.Checkout;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.BookService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.Customer_orderItemsService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.OrdersService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.UserService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.web.dto.UserRegistrationDto;
import net.bytebuddy.utility.RandomString;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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



    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

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

            //sendVerificationEmail(registrationDto);

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
        mailContent += "<a>http://localhost:8080/registration/verify/"+ registrationDto.getVerificationCode() +"/"+registrationDto.getUsername()+"</a>";
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
}
