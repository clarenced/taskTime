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
        assertEquals(actual.getSuccess().title(), "Original Task");
        assertEquals(actual.getSuccess().description(), "description to be updated");
        assertEquals(actual.getSuccess().status(), TaskTimeApi.TaskStatus.DONE);
    }

    @Test
    void should_not_update_task_when_description_is_empty() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), Optional.empty(), of(TaskTimeApi.TaskStatus.DONE));
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        var actual = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(actual.isSuccess());
        assertEquals(actual.getSuccess().title(), "title to be updated");
        assertEquals(actual.getSuccess().description(), "original Description");
        assertEquals(actual.getSuccess().status(), TaskTimeApi.TaskStatus.DONE);
    }

    @Test
    void should_not_update_task_when_status_is_null() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), Optional.empty());
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        var actual = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(actual.isSuccess());
        assertEquals(actual.getSuccess().title(), "title to be updated");
        assertEquals(actual.getSuccess().description(), "description to be updated");
        assertEquals(actual.getSuccess().status(), TaskTimeApi.TaskStatus.TO_DO);
    }


    @Test
    void should_not_update_task_when_title_is_too_long () {
        var updateTaskDto =
                new TaskTimeApi.UpdateTaskDto(of("title to be updated".repeat(35)), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));
        var originalTask = new TaskTimeApi.TaskDto(1L, "Original Task", "original Description");

        Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> result = TaskUpdator.updateTask(originalTask, updateTaskDto);

        assertTrue(result.isError());
        assertEquals("title", result.getError().field());
        assertEquals("title has more than 30 characters", result.getError().message());

    }
}