package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Book;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if(!bookOptional.isPresent()){
            throw new RuntimeException("Book Not Found");
        }
        return bookOptional.get();
    }

    @Override
    public Long create(Book bookDetails) {
        bookRepository.save(bookDetails);
        return bookDetails.getBookId();
    }

    @Override
    public void update(Long bookId, Book bookDetails) {
        Book currentBook = findById(bookId);

        currentBook.setTitle(bookDetails.getTitle());
        currentBook.setISBN(bookDetails.getISBN());
        currentBook.setDescription(bookDetails.getDescription());
        currentBook.setPrice(bookDetails.getPrice());
        currentBook.setPublisher(bookDetails.getPublisher());
        currentBook.setPubdate(bookDetails.getPubdate());
        currentBook.setAuthors(bookDetails.getAuthors());
        currentBook.setCategories(bookDetails.getCategories());

        bookRepository.save(currentBook);
    }

    @Override
    public void delete(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Page<Book> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.bookRepository.findAll(pageable);
    }
}
