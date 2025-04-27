package io.github.clarenced.tasktime.api;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Validate the task before creating it.
     * @param createTaskDto The task creation request
     * @return A Result indicating success or containing an error
     */
    public Result<Void, TaskTimeApi.ErrorDto> createTask(TaskTimeApi.CreateTaskDto createTaskDto) {
        return TaskValidator.validateTask(createTaskDto)
                .map(validDto -> {
                    taskRepository.createTask(validDto);
                    return null;
                });
    }

    public List<TaskTimeApi.TaskDto> getTasks() {
        return this.taskRepository.getTasks();
    }

    public Optional<TaskTimeApi.TaskDto> findTaskById(int taskId) {
        return getTasks().stream()
                .filter(task -> task.id() == taskId)
                .findFirst();
    }
}