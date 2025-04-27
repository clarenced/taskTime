package io.github.clarenced.tasktime.api;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.TaskRepository;
import io.github.clarenced.tasktime.tasks.TaskService;
import io.github.clarenced.tasktime.tasks.TaskTimeApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private final TaskRepository taskRepository = new TaskRepository();
    private final TaskService taskService = new TaskService(taskRepository);


    @Test
    @DisplayName("Should create task when validation succeeds")
    void should_create_task_when_validation_succeeds() {
        TaskTimeApi.CreateTaskDto createTaskDto = new TaskTimeApi.CreateTaskDto("Test Task", "Test Description");

        Result<Void, TaskTimeApi.ErrorDto> result = taskService.createTask(createTaskDto);
        
        assertTrue(result.isSuccess());
        assertTrue(taskRepository.newTaskIsAdded("Test Task"));
    }

    @Test
    @DisplayName("Should not create task when validation fails")
    void should_not_create_task_when_validation_fails() {
        TaskTimeApi.CreateTaskDto createTaskDto = new TaskTimeApi.CreateTaskDto("", "Test Description");

        Result<Void, TaskTimeApi.ErrorDto> result = taskService.createTask(createTaskDto);
        
        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title is empty", result.getError().message());
        assertTrue(taskRepository.noNewTaskIsCreated());
    }
}