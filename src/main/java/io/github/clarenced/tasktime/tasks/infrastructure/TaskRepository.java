package io.github.clarenced.tasktime.tasks.infrastructure;

import io.github.clarenced.tasktime.tasks.domain.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void createTask(Task task);

    List<Task> getTasks();

    void updateTask(Task updatedTask);

    Optional<Task> findTaskById(Long taskId);
}
