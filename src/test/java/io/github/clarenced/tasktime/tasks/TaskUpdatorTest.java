package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;

class TaskUpdatorTest {

    @Test
    void updateTaskWithNewValues () {
        var expected = new TaskTimeApi.TaskDto(1L, "Test Task", "Test Description", TaskTimeApi.TaskStatus.TO_DO);
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("Test Task"), of("Test Description"), of(TaskTimeApi.TaskStatus.TO_DO));
        TaskTimeApi.TaskDto originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        var result = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(result.isSuccess());
        assertEquals(result.getSuccess(), expected);
    }

    @Test
    void should_not_update_task_when_title_is_empty() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(Optional.empty(), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));
        TaskTimeApi.TaskDto originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        var actual = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(actual.isSuccess());
        assertEquals("Original Task", actual.getSuccess().title());
        assertEquals("description to be updated", actual.getSuccess().description());
        assertEquals(TaskTimeApi.TaskStatus.DONE, actual.getSuccess().status());
    }

    @Test
    void should_not_update_task_when_description_is_empty() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), Optional.empty(), of(TaskTimeApi.TaskStatus.DONE));
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        var actual = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(actual.isSuccess());
        assertEquals("title to be updated", actual.getSuccess().title());
        assertEquals("original Description", actual.getSuccess().description());
        assertEquals(TaskTimeApi.TaskStatus.DONE, actual.getSuccess().status());
    }

    @Test
    void should_not_update_task_when_status_is_null() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), Optional.empty());
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        var actual = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(actual.isSuccess());
        assertEquals("title to be updated", actual.getSuccess().title());
        assertEquals("description to be updated", actual.getSuccess().description());
        assertEquals(TaskTimeApi.TaskStatus.TO_DO, actual.getSuccess().status());
    }


    @Test
    void should_return_error_task_when_title_is_too_long () {
        var updateTaskDto =
                new TaskTimeApi.UpdateTaskDto(of("title to be updated".repeat(35)), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> result = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title has more than 30 characters", result.getError().message());
    }

    @Test
    void should_returns_error_task_when_title_is_too_short () {
        var updateTaskDto =
                new TaskTimeApi.UpdateTaskDto(of("tit"), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> result = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title must have at least 5 characters", result.getError().message());
    }

    @Test
    void should_return_error_when_task_description_is_too_long () {
        var updateTaskDto =
                new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated".repeat(301)), of(TaskTimeApi.TaskStatus.DONE));
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> result = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(result.isError());
        assertEquals("description", result.getError().field());
        assertEquals("description has more than 300 characters", result.getError().message());
    }
}