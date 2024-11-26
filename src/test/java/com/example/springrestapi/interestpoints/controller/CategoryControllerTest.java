package com.example.springrestapi.interestpoints.controller;

import com.example.springrestapi.config.TestSecurityConfig;
import com.example.springrestapi.interestpoints.model.Category;
import com.example.springrestapi.interestpoints.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(TestSecurityConfig.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldCreateCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("New Category");
        category.setSymbol("New Symbol");
        category.setDescription("New Description");

        given(categoryRepository.existsByName(category.getName())).willReturn(false);
        given(categoryRepository.save(category)).willReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Category\",\"symbol\":\"New Symbol\",\"description\":\"New Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"New Category\",\"symbol\":\"New Symbol\",\"description\":\"New Description\"}"));
    }
}