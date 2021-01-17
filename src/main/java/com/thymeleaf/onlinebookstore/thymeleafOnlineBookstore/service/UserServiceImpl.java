package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Role;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.UserRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getUsername(),passwordEncoder.encode(registrationDto.getPassword()),registrationDto.getUser_fName(),registrationDto.getUser_lName(),registrationDto.getUser_email(),registrationDto.getUser_phoneNo(),registrationDto.getUser_address(), Arrays.asList(new Role(registrationDto.getUserRole())), registrationDto.isEnabled(),registrationDto.getVerificationCode());

        return userRepository.save(user);
    }

    @Override
    public boolean passwordencode(String sentpassword, String password) {
        return passwordEncoder.matches(sentpassword, password);
    }

    @Override
    public boolean isUniqueEmailViolated(Long id, String email) {
        boolean isuniqueEmailViolated = false;

        User userByEmail = userRepository.getUserByEmail(email);
        boolean isCreatingNew = (id == null || id == 0);

        if(isCreatingNew){
            if(userByEmail != null) isuniqueEmailViolated = true;
        } else {
            if(userByEmail.getId() != id){
                isuniqueEmailViolated = true;
            }
        }
        return isuniqueEmailViolated;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user== null){
            throw  new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), mapRolesToAuthorities(user.getUserRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
       return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }
}
