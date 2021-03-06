package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Student;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.UserRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.UserService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users/")
public class UserController {

    private UserRepository userRepository;


    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("signup")
    public String showSignUpForm(User user){
        return "add-user";
    }

    @GetMapping("list")
    public String showUpdateForm(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin_viewAllUsers";
    }

    @PostMapping("add")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }

        userRepository.save(user);
        return "redirect:list";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("student", user);
        return "update-user";
    }

    @PostMapping("update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "admin_viewAllUsers";
    }

    @GetMapping("adminhome")
    public String login(){
        return "index";
    }

    //junit testing
    public String hello(String name){
        return String.format("Hello, %s", name);
    }
}
