package com.timetable.trackingApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timetable.trackingApp.domain.TopUsers;
import com.timetable.trackingApp.services.TopUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = TopUsersController.class)
class TopUsersControllerTest {
    @Value("/top_users/")
    private String basePath;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopUserService service;

    @InjectMocks
    private TopUsersController controller;

    @Autowired
    private ObjectMapper objectMapper;

    private TopUsers testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new TopUsers();
        testEntity.setId("1");
        testEntity.setUserId("userId");
        testEntity.setRating(10);


    }

    @Test
    @WithMockUser
    void getAll() throws Exception {
        List<TopUsers> allEntities = Collections.singletonList(testEntity);

        when(service.getAll()).thenReturn(allEntities);

        mockMvc.perform(MockMvcRequestBuilders.get(basePath + "getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testEntity.getId()))
                .andExpect(jsonPath("$[0].userId").value(testEntity.getUserId()))
                .andExpect(jsonPath("$[0].rating").value(testEntity.getRating()));

        verify(service).getAll();
    }

    @Test
    @WithMockUser
    void top() throws Exception {
        doNothing().when(service).deleteUsersExceptTopTen();

        mockMvc.perform(MockMvcRequestBuilders.get(basePath + "top10"))
                .andExpect(status().isOk());

        verify(service).deleteUsersExceptTopTen();
    }

    @Test
    @WithMockUser
    void get() throws Exception {
        when(service.get(anyString())).thenReturn(testEntity);

        mockMvc.perform(MockMvcRequestBuilders.get(basePath + "get")
                        .param("documentId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testEntity.getId()))
                .andExpect(jsonPath("$.userId").value(testEntity.getUserId()))
                .andExpect(jsonPath("$.rating").value(testEntity.getRating()));

        verify(service).get(anyString());
    }
}