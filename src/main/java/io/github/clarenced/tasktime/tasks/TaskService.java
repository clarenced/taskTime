package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;
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

    public Optional<TaskTimeApi.TaskDto> findTaskById(Long taskId) {
        return getTasks().stream()
                .filter(task -> task.id().equals(taskId))
                .findFirst();
    }

    public Result<Void, TaskTimeApi.ErrorDto> updateTask(Long taskId, TaskTimeApi.UpdateTaskDto createTaskDto) {
        Optional<TaskTimeApi.TaskDto> task = findTaskById(taskId);
        if(task.isEmpty()) {
            return Result.error(new TaskTimeApi.ErrorDto("taskId", "Task not found"));
        }
        var updatedTask = TaskUpdator.updateTask(task.get(), createTaskDto);
        if(updatedTask.isError()) {
            return Result.error(updatedTask.getError());
        }

        return updatedTask.map(_ -> {
            taskRepository.updateTask(updatedTask.getSuccess());
            return null;
        });
    }

}