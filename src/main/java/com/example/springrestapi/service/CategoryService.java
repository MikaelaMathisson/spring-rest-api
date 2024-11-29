package com.example.springrestapi.service;

import com.example.springrestapi.model.Category;
import com.example.springrestapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        if (category.getId() != null && categoryRepository.existsById(category.getId())) {
            throw new IllegalArgumentException("Category with this ID already exists");
        }
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        category.setName(category.getName() + " - Updated");
        return categoryRepository.save(category);
    }
}