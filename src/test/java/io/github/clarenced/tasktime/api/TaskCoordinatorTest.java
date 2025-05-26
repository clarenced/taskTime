package io.github.clarenced.tasktime.api;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.infrastructure.FakeTaskRepository;
import io.github.clarenced.tasktime.tasks.application.TaskCoordinator;
import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;

public class TaskCoordinatorTest {

    private final FakeTaskRepository fakeTaskRepository = new FakeTaskRepository();
    private final TaskCoordinator taskCoordinator = new TaskCoordinator(fakeTaskRepository);


    @Test
    @DisplayName("Should create task when validation succeeds")
    void should_create_task_when_validation_succeeds() {
        TaskTimeApi.CreateTaskDto createTaskDto = new TaskTimeApi.CreateTaskDto("Test Task", "Test Description");

        Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.createTask(createTaskDto);
        
        assertTrue(result.isSuccess());
        assertTrue(fakeTaskRepository.newTaskIsAdded("Test Task"));
    }

    @Test
    @DisplayName("Should not create task when validation fails")
    void should_not_create_task_when_validation_fails() {
        TaskTimeApi.CreateTaskDto createTaskDto = new TaskTimeApi.CreateTaskDto("", "Test Description");

        Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.createTask(createTaskDto);
        
        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title is empty", result.getError().message());
        assertTrue(fakeTaskRepository.noNewTaskIsCreated());
    }

    @Test
    @DisplayName("should update task when validation succeeds")
    void should_update_task_when_validation_succeeds() {
        TaskTimeApi.UpdateTaskDto updateTaskDto =
                new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));

        Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.updateTask(1L, updateTaskDto);
        assertTrue(result.isSuccess());

        assertTrue(fakeTaskRepository.assertThatTitle(1L, "title to be updated"));
        assertTrue(fakeTaskRepository.assertThatDescription(1L, "description to be updated"));
        assertTrue(fakeTaskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.DONE));
    }


    @Test
    @DisplayName("should not update task when task not found")
    void should_not_update_task_when_task_not_found() {
        Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.updateTask(10L, new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE)));

        assertTrue(result.isError());
        assertEquals("Task with id 10 does not exist", result.getError().message());
        assertEquals("taskId", result.getError().field());
        assertTrue(fakeTaskRepository.noNewTaskIsCreated());
    }


    @Test
    @DisplayName("should not update task when title is empty")
    void should_not_update_task_when_title_is_empty() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(Optional.empty(), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));
        Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.updateTask(1L, updateTaskDto);

        assertTrue(result.isSuccess());
        assertTrue(fakeTaskRepository.assertThatTitle(1L, "Slides !!!"));
        assertTrue(fakeTaskRepository.assertThatDescription(1L, "description to be updated"));
        assertTrue(fakeTaskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.DONE));
    }

    @Test
    @DisplayName("should not update task when description is empty")
    void should_not_update_task_when_description_is_empty() {
      var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), Optional.empty(), of(TaskTimeApi.TaskStatus.DONE));
      Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.updateTask(1L, updateTaskDto);
      assertTrue(result.isSuccess());
      assertTrue(fakeTaskRepository.assertThatTitle(1L, "title to be updated"));
      assertTrue(fakeTaskRepository.assertThatDescription(1L, "Prepare slides for the Spring meetup"));
      assertTrue(fakeTaskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.DONE));
    }

    @Test
    @DisplayName("should not update task when status is empty")
    void should_not_update_task_when_status_is_null() {
     var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), Optional.empty());
     Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.updateTask(1L, updateTaskDto);
     assertTrue(result.isSuccess());
     assertTrue(fakeTaskRepository.assertThatTitle(1L, "title to be updated"));
     assertTrue(fakeTaskRepository.assertThatDescription(1L, "description to be updated"));
     assertTrue(fakeTaskRepository.assertThatStatus(1L, TaskTimeApi.TaskStatus.TO_DO));
    }

    @Test
    void should_not_update_task_when_validation_fails() {
        var updateTaskDto =
                new TaskTimeApi.UpdateTaskDto(of("title to be updated".repeat(35)), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));

        Result<Void, TaskTimeApi.ErrorDto> result = taskCoordinator.updateTask(1L, updateTaskDto);

        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title has more than 30 characters", result.getError().message());
    }
}