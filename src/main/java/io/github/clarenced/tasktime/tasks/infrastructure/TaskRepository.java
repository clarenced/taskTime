package io.github.clarenced.tasktime.tasks.infrastructure;

import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;
import io.github.clarenced.tasktime.tasks.domain.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void createTask(Task task);

    List<TaskTimeApi.TaskDto> getTasks();

    void updateTask(TaskTimeApi.TaskDto updatedTask);

    Optional<TaskTimeApi.TaskDto> findTaskById(Long taskId);
}
