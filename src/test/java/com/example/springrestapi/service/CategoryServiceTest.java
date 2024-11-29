package com.example.springrestapi.service;

import com.example.springrestapi.model.Category;
import com.example.springrestapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        categoryService.getAllCategories();
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(new Category()));
        Optional<Category> category = categoryService.getCategoryById(id);
        assertTrue(category.isPresent());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setName("Test Category");
        when(categoryRepository.existsByName(category.getName())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);
        assertNotNull(createdCategory);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testCreateCategoryWithExistingName() {
        Category category = new Category();
        category.setName("Existing Category");
        when(categoryRepository.existsByName(category.getName())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
        verify(categoryRepository, never()).save(category);
    }
}