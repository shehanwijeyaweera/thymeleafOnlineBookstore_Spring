package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Author;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService{

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public void saveAuthor(Author author) {
        this.authorRepository.save(author);
    }

    @Override
    public Author getAuthorById(long author_id) {
        Optional<Author> optional = authorRepository.findById(author_id);
        Author author = null;

        if (optional.isPresent()){
            author = optional.get();
        }
        else {
            throw  new RuntimeException("author not found for id: " + author_id);
        }

        return author;
    }

    @Override
    public void deleteAuthorById(long author_id) {
        this.authorRepository.deleteById(author_id);
    }
}
