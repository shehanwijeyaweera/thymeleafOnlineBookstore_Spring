package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.CartItem;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.AuthorService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.BookService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

        return "customer_view-book";
    }

    //show a single book details
    @RequestMapping("show/{book_id}")
    public String showSingleBook(@PathVariable("book_id")long book_id, Model model){
        Book book = bookService.findById(book_id);
        model.addAttribute("book", book);
        return "customer_showSingleBookById";
    }

    //view cart
    @GetMapping("cart")
    public String viewCart(){
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

}