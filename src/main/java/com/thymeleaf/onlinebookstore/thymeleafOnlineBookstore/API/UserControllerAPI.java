package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.API;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.BookService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.Customer_orderItemsService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.OrdersService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/books")
    public List<Book> getallbooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/book/{id}")
    public Book getbookdetails(@PathVariable(value  = "id") Long bookid){
        return bookService.findById(bookid);
    }

    @GetMapping("/login/{username}/{password}")
    public JSONObject login(@PathVariable(value = "username") String username, @PathVariable(value = "password") String password){
        JSONObject obj = new JSONObject();
        User user = new User();
        user = userRepository.findByUsername(username);
        String pass = user.getPassword();
        if(user !=null){
            if(userService.passwordencode(password,pass)){

                obj.put("user", user);
                obj.put("Response", "Correct");
                obj.put("Role", user.getUserRole());
                return obj;
            }
            else {

                obj.put("username", user.getUsername());
                obj.put("userRole", user.getUserRole());
                obj.put("Response", "Wrong");
                return obj;
            }
        }else{
            obj.put("username", "Error");
            obj.put("userRole", "Error");
            obj.put("Response", "Error");
            return obj;
        }

    }

    @GetMapping("/admindashboard")
    public JSONObject dashboardDetails(){
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
    public List<Customer_orders> getAllCustomerOrders(){
        List<Customer_orders> customer_orders = ordersRepository.getAllNewOrders();
        return customer_orders;
    }

    @GetMapping("/custpendingorders")
    public List<Customer_orders> getAllPendingOrders(){
        List<Customer_orders> customer_orders = ordersRepository.getAllPendingOrders();
        return customer_orders;
    }

    @GetMapping("/custshippedorders")
    public List<Customer_orders> getAllShippedOrders(){
        List<Customer_orders> customer_orders = ordersRepository.getAllshippedOrders();
        return customer_orders;
    }

}
