package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository;

import java.util.List;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface StudentRepository extends CrudRepository <Student, Long> {
    List<Student> findByName(String name);
}