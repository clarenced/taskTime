package io.github.clarenced.tasktime.api;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@Testcontainers
public class TaskTimeApiTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

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
    }

    @Test
    @DisplayName("Should return 404 when task not found")
    void should_return_404_when_task_not_found(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/api/tasks/{taskId}", 5))
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
                .andExpect(jsonPath("$[2].description").value("description"))
                .andExpect(jsonPath("$[2].status").value("TO_DO"));
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

    @Test
    @DisplayName("should return 404 not found when updating task with unknown id")
    void should_return_404_not_found_when_updating_task_with_unknown_id(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/api/tasks/{taskId}", 4)
                .contentType("application/json")
                .content("{\"title\":\"title\",\"description\":\"description\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Task with id 4 does not exist"));
    }

    @Test
    @DisplayName("should return 200 when updating task with valid data")
    void should_return_200_when_updating_task_with_valid_data(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/api/tasks/{taskId}", 1)
                .contentType("application/json")
                .content("{\"title\":\"title to be updated\",\"description\":\"description to be updated\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/{taskId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title").value("title to be updated"))
                .andExpect(jsonPath("$.description").value("description to be updated"))
                .andExpect(jsonPath("$.id").value(1));
    }
}