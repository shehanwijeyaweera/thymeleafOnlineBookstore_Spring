package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.API;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Category;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Role;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.User;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.CategoryRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.UserRepository;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.CategoryServiceImpl;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.UserService;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class UserControllerAPITest {

    @MockBean
    private UserRepository userRepo;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    public void testEmailValidation() throws Exception {
            Long userID = null;
            String userEmail = "test123@gmail.com";

            Mockito.when(userRepo.getUserByEmail(userEmail)).thenReturn(null);

            boolean result = service.isUniqueEmailViolated(userID, userEmail);

            assertFalse(result);
    }

    @Test
    public void testEmailValidationEmailNotUnique() throws Exception {
        Long userID = null;
        String userEmail = "test123@gmail.com";

        User user = new User();
        user.setUser_email(userEmail);
        user.setId(Long.valueOf(1));

        Mockito.when(userRepo.getUserByEmail(userEmail)).thenReturn(user);

        boolean result = service.isUniqueEmailViolated(userID, userEmail);

        assertTrue(result);
    }

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void checkUniqueCategory() throws Exception {
        Long id = null;
        String name = "Fantasy";

        Category category = new Category();
        category.setCategory_id(Long.valueOf(1));
        category.setCategory_name("Fantasy");

       Mockito.when(categoryRepository.findByCategoryName(name)).thenReturn(category);

       String result = categoryService.checkUnique(id, name);

       assertEquals(result, "DuplicatedName");
    }
}