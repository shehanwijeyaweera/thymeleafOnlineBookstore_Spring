package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void saveCategory(Category category);
    Category getCategoryById(long category_id);
    void deleteCategoryById(long category_id);
}
