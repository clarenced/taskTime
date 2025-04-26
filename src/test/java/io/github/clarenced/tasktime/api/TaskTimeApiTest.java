package io.github.clarenced.tasktime.api;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskTimeApiTest {

    @Test
    @DisplayName("Should return list of current tasks")
    void should_return_list_current_tasks(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Prepare slides for the Spring meetup"))
                .andExpect(jsonPath("$[0].description").value("Prepare slides for the Spring meetup"))
                .andExpect(jsonPath("$[1].title").value("Go to the theater"))
                .andExpect(jsonPath("$[1].description").value("Go to the theater"));
    }


}
