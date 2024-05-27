package com.timetable.trackingApp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.timetable.trackingApp.domain.Categories;
import com.timetable.trackingApp.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerTestTEMP {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;
    private Categories category;

    @BeforeEach
    void setUp() {
        category = new Categories("1", "Test Category");
//        categoryService.create(category);
    }

    @Test
    @WithMockUser
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(category.getId()))
                .andExpect(jsonPath("$[0].name").value(category.getName()));

        verify(categoryService).getAll();
    }

    @Test
    @WithMockUser
    void createCategory() throws Exception {
        Categories entity = new Categories();
        entity.setName("Test Category");

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk());
    }

    @Test
    void get() {

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}