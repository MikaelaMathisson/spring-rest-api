package com.example.springrestapi.interestpoints.controller;

import com.example.springrestapi.interestpoints.model.Category;
import com.example.springrestapi.interestpoints.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser
    public void shouldReturnAllCategories() throws Exception {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setSymbol("Symbol 1");
        category1.setDescription("Description 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setSymbol("Symbol 2");
        category2.setDescription("Description 2");

        List<Category> allCategories = Arrays.asList(category1, category2);

        given(categoryRepository.findAll()).willReturn(allCategories);

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'name':'Category 1','symbol':'Symbol 1','description':'Description 1'},{'id':2,'name':'Category 2','symbol':'Symbol 2','description':'Description 2'}]"));
    }
}