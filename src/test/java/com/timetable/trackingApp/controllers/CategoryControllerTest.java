package com.timetable.trackingApp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.timetable.trackingApp.domain.Categories;
import com.timetable.trackingApp.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @InjectMocks
    private CategoryController categoryController;

    @Autowired
    private ObjectMapper objectMapper;
    private Categories testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Categories("1", "Test Category");
    }

    @Test
    @WithMockUser
    public void getAll() throws Exception {
        List<Categories> allCategories = Collections.singletonList(testCategory);

        when(categoryService.getAll()).thenReturn(allCategories);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testCategory.getId()))
                .andExpect(jsonPath("$[0].name").value(testCategory.getName()));

        verify(categoryService).getAll();
    }

    @Test
    @WithMockUser
    void createCategory() throws Exception {
        String createId = "abc123";

        when(categoryService.create(any(Categories.class))).thenReturn(createId);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
//                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(createId));

        verify(categoryService).create(any(Categories.class));
    }

    @Test
    @WithMockUser
    void get() throws Exception {
        when(categoryService.get(anyString())).thenReturn(testCategory);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/get")
                        .param("documentId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCategory.getId()))
                .andExpect(jsonPath("$.name").value(testCategory.getName()));

        verify(categoryService).get(anyString());
    }

    @Test
    @WithMockUser
    void update() throws Exception {
        String expectedResponse = "Expected Response";

        when(categoryService.update(any(Categories.class))).thenReturn(expectedResponse);

        mockMvc.perform(put("/categories/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
//                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(expectedResponse));

        verify(categoryService).update(any(Categories.class));
    }

    @Test
    @WithMockUser
    void delete() throws Exception {
        String expectedResponse = "Successfully deleted";

        when(categoryService.delete(anyString())).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/delete")
                        .with(csrf())
                        .param("documentId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(expectedResponse));

        verify(categoryService).delete(anyString());
    }
}