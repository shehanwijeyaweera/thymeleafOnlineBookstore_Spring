package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.*;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.OrdersRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.RefundRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.UserRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/customer/")
public class CustomerBookController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private Customer_orderItemsService customer_orderItemsService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private RefundRepository refundRepository;

    //display list of books
    @GetMapping("book")
    public String ViewAllBooks(Model model){
        return findPaginated(1, "title", "asc", model);
    }

    @GetMapping("page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField , @RequestParam("sortDir") String sortDir, Model model){
        int pageSize = 6;

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

        return "Customer_viewAllBooks";
    }

    //show a single book details
    @RequestMapping("show/{book_id}")
    public String showSingleBook(@PathVariable("book_id")long book_id, Model model){
        Book book = bookService.findById(book_id);
        model.addAttribute("book", book);
        return "Customer_singleBookView";
    }

    //view cart
    @GetMapping("cart")
    public String viewCart(ModelMap modelMap, HttpSession session){
        modelMap.put("total" , total(session));
        return "cart";
    }

    //add cart function
    @RequestMapping("buy/{bookId}")
    public String buy(@PathVariable("bookId") Long bookId, Model model, HttpSession session){
        if(session.getAttribute("cart")==null){
            List<CartItem> cartItems = new ArrayList<CartItem>();
            cartItems.add(new CartItem(bookService.findById(bookId), 1));
            session.setAttribute("cart", cartItems);
        }
        else {
            List<CartItem> cartItems =(List<CartItem>) session.getAttribute("cart");
            int index = isExists(bookId, cartItems);
            if (index == -1){
                cartItems.add(new CartItem(bookService.findById(bookId), 1));
            }else {
                int quantity = cartItems.get(index).getQuantity() + 1;
                cartItems.get(index).setQuantity(quantity);
            }
            session.setAttribute("cart", cartItems);
        }
        return "redirect:/customer/cart";
    }

    private int isExists(Long bookId, List<CartItem> cartItems){
        for(int i = 0; i < cartItems.size(); i++){
            if(cartItems.get(i).getBook().getBookId() == bookId){
                return i;
            }
        }
        return -1;
    }

    //remove book from cart function
    @RequestMapping("remove/{bookId}")
    public String remove(@PathVariable Long bookId, Model model, HttpSession session){
        List<CartItem> cartItems =(List<CartItem>) session.getAttribute("cart");
        int index = isExists(bookId, cartItems);
        cartItems.remove(index);
        session.setAttribute("cart", cartItems);
        return  "redirect:/customer/cart";
    }

    //update the quantity of book and show sub total
    @RequestMapping("cart/update")
    public String update(HttpServletRequest request, HttpSession session){
        String[] quantities = request.getParameterValues("quantity");
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
        for (int i=0; i<cartItems.size(); i++){
            cartItems.get(i).setQuantity(Integer.parseInt(quantities[i]));
        }
        session.setAttribute("cart",cartItems);
        return  "redirect:/customer/cart";
    }

    private double total(HttpSession session){
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
        double s = 0;
        if(session.getAttribute("cart")!=null) {
            for (CartItem cartItem : cartItems) {
                s += cartItem.getQuantity() * cartItem.getBook().getPrice().doubleValue();
            }
        }
        return s;
    }

    @RequestMapping("checkout/{total}")
    public String checkout(@PathVariable("total") double total,HttpSession session){

        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user= userRepository.findByUsername(username);


        if(user.getId()==null){
            return "redirect:/";
        }else {
            //order creation
            Customer_orders orders = new Customer_orders();
            orders.setUser(user);
            orders.setDatecreation(new Date());
            orders.setName("New Order");
            orders.setStatus("Pending");
            orders.setTotal(total);
            ordersService.saveOrder(orders);
            Long orderId = orders.getId();

            // add order items
            List<CartItem> cartItems = (List<CartItem>)session.getAttribute("cart");
            for(CartItem cartItem: cartItems){
                Customer_orderItems customer_orderItems = new Customer_orderItems();
                customer_orderItems.setBook(cartItem.getBook());
                customer_orderItems.setCustomer_orders(orders);
                customer_orderItems.setPrice(cartItem.getBook().getPrice());
                customer_orderItems.setQuantity(cartItem.getQuantity());

                customer_orderItemsService.saveOrderItem(customer_orderItems);

                //Delete cart
                session.removeAttribute("cart");
            }
            return "redirect:/customer/orders/thanks";
        }
    }

    @GetMapping("/orders/thanks")
    public String ViewThanksPage(){
        return "thanks";
    }

    @GetMapping("order")
    public String ShowCustomerOrders(Model model){

        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user= userRepository.findByUsername(username);

        List<Customer_orders> ordersList = ordersService.getCustomerOrders(user.getId());
        model.addAttribute("customer_orders", ordersList);

        return "Cust_showAllOrders";
    }

    @RequestMapping("order/{order_id}")
    public String showSingleOrder(@PathVariable("order_id")long order_id, Model model){
        Customer_orders customer_orders = ordersService.findById(order_id);
        List<Customer_orderItems> bookList= customer_orderItemsService.getMyItems(order_id);
        model.addAttribute("customer_order", customer_orders);
        model.addAttribute("items",bookList);
        return "Cust_viewSingleOrderDetails";
    }

    //show refund request form
    @RequestMapping("refundreq/{order_id}")
    public String RequestRefundForm(@PathVariable("order_id") long order_id, Model model, Refund refund){
        model.addAttribute("order_id", order_id);
        return "Cust_refundReqpage";
    }

    @PostMapping("refundreq/save/{order_id}")
    public String RequestRefundSave(@PathVariable("order_id") Long order_id, @ModelAttribute("refund") Refund refund){
        Customer_orders customer_orders = new Customer_orders();

       customer_orders = ordersRepository.findOrderbyId(order_id);

        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user= userRepository.findByUsername(username);

        Refund refund1 = new Refund();
        refund1.setReason(refund.getReason());
        refund1.setCustomer_orders(customer_orders);
        refund1.setReqDate(new Date());
        refund1.setUser(user);

        refundRepository.save(refund1);

        return "redirect:/customer/order";
    }



}
