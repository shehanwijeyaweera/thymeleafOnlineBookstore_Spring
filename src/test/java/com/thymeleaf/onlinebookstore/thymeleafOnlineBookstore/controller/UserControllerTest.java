package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void hello() {
        UserController controller = new UserController(null);
        String response = controller.hello("World");

        assertEquals("Hello, World", response);
    }
}