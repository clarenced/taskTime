package io.github.clarenced.tasktime.api;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


    @Test
    @DisplayName("Should return task by id")
    void should_return_task_by_id(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/api/tasks/{taskId}", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title").value("Go to the theater"))
                .andExpect(jsonPath("$.description").value("Go to the theater"))
                .andExpect(jsonPath("$.id").value(3));

        mockMvc.perform(get("/api/tasks/{taskId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title").value("Prepare slides for the Spring meetup"))
                .andExpect(jsonPath("$.description").value("Prepare slides for the Spring meetup"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Should return 404 when task not found")
    void should_return_404_when_task_not_found(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/api/tasks/{taskId}", 4))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Should create new task")
    void should_create_task(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content("{\"title\":\"New task\",\"description\":\"description\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[2].title").value("New task"))
                .andExpect(jsonPath("$[2].description").value("description"));
    }

    @Test
    @DisplayName("Should bad request when creating tasks with empty description")
    void should_bad_request_when_creating_tasks_with_empty_title(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content("{\"title\":\"\",\"description\":\"description\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("title is empty"))
                .andExpect(jsonPath("$.field").value("title"));

    }


    @Test
    @DisplayName("Should return bad request when title is empty")
    void should_return_bad_request_when_description_is_empty(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content("{\"title\":\"title\",\"description\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("description is empty"))
                .andExpect(jsonPath("$.field").value("description"));
    }
}