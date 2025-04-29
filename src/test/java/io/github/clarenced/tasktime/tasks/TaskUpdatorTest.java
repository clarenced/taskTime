package io.github.clarenced.tasktime.tasks;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;

class TaskUpdatorTest {

    @Test
    void updateTaskWithNewValues () {
        var expected = new TaskTimeApi.TaskDto(1L, "Test Task", "Test Description", TaskTimeApi.TaskStatus.TO_DO);
        var actual = TaskUpdator.updateTask(
                new TaskTimeApi.TaskDto(1L, "Original Task", "original Description"),
                new TaskTimeApi.UpdateTaskDto(of("Test Task"),
                        of("Test Description"),
                        of(TaskTimeApi.TaskStatus.TO_DO)));
        assertEquals(actual, expected);
    }

    @Test
    void should_not_update_task_when_title_is_empty() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(Optional.empty(), of("description to be updated"), of(TaskTimeApi.TaskStatus.DONE));
        var actual = TaskUpdator.updateTask(
                new TaskTimeApi.TaskDto(1L, "Original Task", "original Description"),
                updateTaskDto);
        assertEquals(actual.title(), "Original Task");
        assertEquals(actual.description(), "description to be updated");
        assertEquals(actual.status(), TaskTimeApi.TaskStatus.DONE);
    }

    @Test
    void should_not_update_task_when_description_is_empty() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), Optional.empty(), of(TaskTimeApi.TaskStatus.DONE));
        var actual = TaskUpdator.updateTask(
                new TaskTimeApi.TaskDto(1L, "Original Task", "original Description"),
                updateTaskDto);
        assertEquals(actual.title(), "title to be updated");
        assertEquals(actual.description(), "original Description");
        assertEquals(actual.status(), TaskTimeApi.TaskStatus.DONE);
    }

    @Test
    void should_not_update_task_when_status_is_null() {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(of("title to be updated"), of("description to be updated"), Optional.empty());
        var actual = TaskUpdator.updateTask(
                new TaskTimeApi.TaskDto(1L, "Original Task", "original Description"),
                updateTaskDto);
        assertEquals(actual.title(), "title to be updated");
        assertEquals(actual.description(), "description to be updated");
        assertEquals(actual.status(), TaskTimeApi.TaskStatus.TO_DO);
    }
}