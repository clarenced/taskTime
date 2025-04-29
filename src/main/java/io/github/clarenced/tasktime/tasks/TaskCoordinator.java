package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaskCoordinator {

    private final TaskRepository taskRepository;

    public TaskCoordinator(TaskRepository taskRepository) {
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

    public Optional<TaskTimeApi.TaskDto> findTaskById(Long taskId) {
        return this.taskRepository.findTaskById(taskId);
    }

    public Result<Void, TaskTimeApi.ErrorDto> updateTask(Long taskId, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        return TaskExistenceChecker.validateExistingTask(taskId, this.findTaskById(taskId))
                .flatMap(existingTask -> performTaskUpdate(existingTask, updateTaskDto));
    }

    private Result<Void, TaskTimeApi.ErrorDto> performTaskUpdate(TaskTimeApi.TaskDto existingTask, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        return TaskUpdator.updateTask(existingTask, updateTaskDto)
                .map(updatedTask -> {
                    taskRepository.updateTask(updatedTask);
                    return null;
                });
    }
}