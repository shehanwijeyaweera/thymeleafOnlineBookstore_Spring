package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.JSON;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.CartItem;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;

import java.util.List;

public class Checkout {

    private User user;

    private double quantity;

    private List<CartItem> cartItems;

    public Checkout(User user, double quantity, List<CartItem> cartItems) {
        this.user = user;
        this.quantity = quantity;
        this.cartItems = cartItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
