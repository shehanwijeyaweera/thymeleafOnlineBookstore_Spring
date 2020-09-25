package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.controller;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.UserRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.UserService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.web.dto.UserRegistrationDto;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserRegistrationDto());
        return "user_registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) throws UnsupportedEncodingException, MessagingException {
        registrationDto.setUserRole("User");
        registrationDto.setEnabled(false);

        String randomcode = RandomString.make(64);

        registrationDto.setVerificationCode(randomcode);

        userService.save(registrationDto);

        sendVerificationEmail(registrationDto);

        return "redirect:/userverifypage?success";
    }

    public void sendVerificationEmail(UserRegistrationDto registrationDto) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "OnlineBookStore team";
        String mailContent = "<p>Dear " + registrationDto.getUser_fName() + ",</p>";
        mailContent += "<p>Please Click the link below to verify to activate your account: </p>";
        mailContent += "<a>http://localhost:8080/registration/verify/"+ registrationDto.getVerificationCode() +"/"+registrationDto.getUsername()+"</a>";
        mailContent += "<p>Thank you <br> The OnlineBookStore Team </p>";

        MimeMessage message =mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("onlinebookstorelk@gmail.com", senderName);
        helper.setTo(registrationDto.getUser_email());
        helper.setSubject(subject);

        helper.setText(mailContent, true);

        mailSender.send(message);
    }

    @GetMapping("/verify/{verificationcode}/{username}")
    public String verifyfunction(@PathVariable("verificationcode")String code, @PathVariable("username")String username){
        User user = userRepository.findByUsername(username);
        user.setEnabled(true);
        userRepository.save(user);
        return "emailverified";
    }
}
