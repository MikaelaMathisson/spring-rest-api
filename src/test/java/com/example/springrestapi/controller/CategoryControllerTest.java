package com.example.springrestapi.controller;

import com.example.springrestapi.model.Category;
import com.example.springrestapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void getAllCategories_returnsAllCategories() {
        List<Category> categories = List.of(new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryController.getAllCategories();

        assertEquals(categories, result);
    }

    @Test
    void getCategoryById_existingId_returnsCategory() {
        Category category = new Category();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        ResponseEntity<Category> result = categoryController.getCategoryById(1L);

        assertEquals(ResponseEntity.ok(category), result);
    }

    @Test
    void getCategoryById_nonExistingId_returnsNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Category> result = categoryController.getCategoryById(1L);

        assertEquals(ResponseEntity.notFound().build(), result);
    }

    @Test
    void createCategory_newCategory_createsCategory() {
        Category category = new Category();
        category.setName("New Category");
        when(categoryRepository.existsByName("New Category")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        ResponseEntity<Category> result = categoryController.createCategory(category);

        assertEquals(ResponseEntity.ok(category), result);
    }

    @Test
    void createCategory_existingCategoryName_returnsBadRequest() {
        Category category = new Category();
        category.setName("Existing Category");
        when(categoryRepository.existsByName("Existing Category")).thenReturn(true);

        ResponseEntity<Category> result = categoryController.createCategory(category);

        assertEquals(ResponseEntity.badRequest().build(), result);
    }
}