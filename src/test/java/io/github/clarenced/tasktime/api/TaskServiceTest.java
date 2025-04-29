package io.github.clarenced.tasktime.api;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.TaskRepository;
import io.github.clarenced.tasktime.tasks.TaskService;
import io.github.clarenced.tasktime.tasks.TaskTimeApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.of;
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

    @Test
    @DisplayName("should update task when validation su")
    void should_update_task_when_validation_succeeds() {
        TaskTimeApi.UpdateTaskDto updateTaskDto =
                new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));

        Result<Void, TaskTimeApi.ErrorDto> result = taskService.updateTask(1L, updateTaskDto);
        assertTrue(result.isSuccess());

        assertTrue(taskRepository.assertThatTitle(1L, "title to be updated"));
        assertTrue(taskRepository.assertThatDescription(1L, "description to be updated"));
        assertTrue(taskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.DONE));
    }


    @Test
    void should_not_update_task_when_task_not_found() {
        Result<Void, TaskTimeApi.ErrorDto> result = taskService.updateTask(10L, new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE)));

        assertTrue(result.isError());
        assertEquals("Task not found", result.getError().message());
        assertEquals("taskId", result.getError().field());
        assertTrue(taskRepository.noNewTaskIsCreated());
    }


    @Test
    void should_not_update_task_when_title_is_empty() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(Optional.empty(), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));
        Result<Void, TaskTimeApi.ErrorDto> result = taskService.updateTask(1L, updateTaskDto);

        assertTrue(result.isSuccess());
        assertTrue(taskRepository.assertThatTitle(1L, "Prepare slides for the Spring meetup"));
        assertTrue(taskRepository.assertThatDescription(1L, "description to be updated"));
        assertTrue(taskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.DONE));
    }

    @Test
    @DisplayName("should not update task when description is empty")
    void should_not_update_task_when_description_is_empty() {
      var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), Optional.empty(), of(TaskTimeApi.TaskStatus.DONE));
      Result<Void, TaskTimeApi.ErrorDto> result = taskService.updateTask(1L, updateTaskDto);
      assertTrue(result.isSuccess());
      assertTrue(taskRepository.assertThatTitle(1L, "title to be updated"));
      assertTrue(taskRepository.assertThatDescription(1L, "Prepare slides for the Spring meetup"));
      assertTrue(taskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.DONE));
    }

    @Test
    @DisplayName("should not update task when status is empty")
    void should_not_update_task_when_status_is_null() {
     var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), Optional.empty());
     Result<Void, TaskTimeApi.ErrorDto> result = taskService.updateTask(1L, updateTaskDto);
     assertTrue(result.isSuccess());
     assertTrue(taskRepository.assertThatTitle(1L, "title to be updated"));
     assertTrue(taskRepository.assertThatDescription(1L, "description to be updated"));
     assertTrue(taskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.TO_DO));
    }

}