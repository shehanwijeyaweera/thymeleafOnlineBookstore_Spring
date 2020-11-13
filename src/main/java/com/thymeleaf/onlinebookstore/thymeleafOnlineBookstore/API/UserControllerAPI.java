package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.API;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Customer_orders;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.UserRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.BookService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.OrdersService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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


    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/books")
    public List<Book> getallbooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/orders")
    public List<Customer_orders> getallorders(){
        return ordersService.getAllOrders();
    }

    @GetMapping("/book/{id}")
    public Book getbookdetails(@PathVariable(value  = "id") Long bookid){
        return bookService.findById(bookid);
    }

    @GetMapping("/login/{username}/{password}")
    public String login(@PathVariable(value = "username") String username, @PathVariable(value = "password") String password){
        User user = new User();
        user = userRepository.findByUsername(username);
        String pass = user.getPassword();
        if(user !=null){
            if(userService.passwordencode(password,pass)){
                return "true";
            }
            else {
                return "false";
            }
        }
        return "fuck";
    }
}
