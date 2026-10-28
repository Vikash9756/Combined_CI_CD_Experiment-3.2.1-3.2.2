package com.vikash.cicd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void getStudentsReturnsJson() throws Exception {
        mvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void createStudentReturns201() throws Exception {
        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Student\",\"course\":\"CSE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void unknownStudentReturns404() throws Exception {
        mvc.perform(get("/api/students/99999"))
                .andExpect(status().isNotFound());
    }
}
