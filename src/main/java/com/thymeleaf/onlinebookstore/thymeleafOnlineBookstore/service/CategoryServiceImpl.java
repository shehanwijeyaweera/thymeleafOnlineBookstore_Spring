package com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.service;

import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.model.Category;
import com.thymeleaf.onlinebookstore.thymeleafOnlineBookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void saveCategory(Category category) {
        this.categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long category_id) {
        Optional<Category> optional= categoryRepository.findById(category_id);
        Category category = null;
        if(optional.isPresent()){
            category = optional.get();
        }
        else {
            throw new RuntimeException("Category not found: " + category_id);
        }
        return category;
    }

    @Override
    public void deleteCategoryById(long category_id) {
        this.categoryRepository.deleteById(category_id);
    }

    @Override
    public String checkUnique(Long id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = categoryRepository.findByCategoryName(name);

        if(isCreatingNew){
            return "DuplicatedName";
        }
        return "Correct";
    }
}
