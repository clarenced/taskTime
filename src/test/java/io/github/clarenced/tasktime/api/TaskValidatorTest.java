package io.github.clarenced.tasktime.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskValidatorTest {

    @Test
    @DisplayName("Should validate task with valid data")
    void should_validate_task_with_valid_data() {
        TaskTimeApi.CreateTaskDto createTaskDto = new TaskTimeApi.CreateTaskDto("Test Task", "Test Description");

        Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result = TaskValidator.validateTask(createTaskDto);

        assertTrue(result.isSuccess());
        assertEquals(createTaskDto, result.getSuccess());
    }

    @Test
    @DisplayName("Should return error when title is empty")
    void should_return_error_when_title_is_empty() {
        TaskTimeApi.CreateTaskDto createTaskDto = new TaskTimeApi.CreateTaskDto("", "Test Description");

        Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result = TaskValidator.validateTask(createTaskDto);

        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title is empty", result.getError().message());
    }

    @Test
    @DisplayName("Should return error when description is empty")
    void should_return_error_when_description_is_empty() {
        TaskTimeApi.CreateTaskDto createTaskDto = new TaskTimeApi.CreateTaskDto("Test Task", "");

        Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result = TaskValidator.validateTask(createTaskDto);

        assertTrue(result.isError());
        assertEquals("description", result.getError().field());
        assertEquals("description is empty", result.getError().message());
    }

    @Test
    @DisplayName("Should return error when title is more than 30 chars")
    void should_return_error_when_title_is_more_than_30_chars() {
        var createTaskDto = new TaskTimeApi.CreateTaskDto("Create a new Unit test", "Test Description".repeat(35));

        Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result = TaskValidator.validateTask(createTaskDto);

        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title has more than 30 characters", result.getError().message());


    }

    @Test
    @DisplayName("Should return error when title is less than 3 chars")
    void should_return_error_when_title_is_less_than_3_chars() {
        var createTaskDto = new TaskTimeApi.CreateTaskDto("Test", "Test Description");

        Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result = TaskValidator.validateTask(createTaskDto);

        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title must have at least 5 characters", result.getError().message());
    }

}
