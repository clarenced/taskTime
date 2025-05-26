package io.github.clarenced.tasktime.tasks.application;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.api.ErrorFactory;
import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;
import io.github.clarenced.tasktime.tasks.domain.Error;
import io.github.clarenced.tasktime.tasks.domain.Task;
import io.github.clarenced.tasktime.tasks.domain.TaskStatus;
import io.github.clarenced.tasktime.tasks.infrastructure.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
        var taskResult = Task.create(createTaskDto.title(), createTaskDto.description(), TaskStatus.TO_DO);

        if (taskResult.isError()) {
            Error error = taskResult.getError();
            return Result.error(new TaskTimeApi.ErrorDto(error.field(), error.message()));
        }
        taskRepository.createTask(taskResult.getSuccess());
        return Result.success();
    }

    public List<TaskTimeApi.TaskDto> getTasks() {
        return this.taskRepository.getTasks().stream()
                .map(TaskCoordinator::toApi)
                .collect(Collectors.toList());
    }

    private static TaskTimeApi.TaskDto toApi(Task task) {
        return new TaskTimeApi.TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                TaskTimeApi.TaskStatus.valueOf(task.getStatus().name()));
    }

    public Optional<TaskTimeApi.TaskDto> findTaskById(Long taskId) {
        return this.taskRepository.findTaskById(taskId)
                .map(TaskCoordinator::toApi);
    }

    public Result<Void, TaskTimeApi.ErrorDto> updateTask(Long taskId, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        return findTaskById(taskId)
                .map(taskDto -> performTaskUpdate(taskDto, updateTaskDto))
                .orElse(Result.error(ErrorFactory.taskNotFound(taskId)));
    }


    private Result<Void, TaskTimeApi.ErrorDto> performTaskUpdate(TaskTimeApi.TaskDto existingTaskDto, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        return TaskUpdator.updateTask(existingTaskDto, updateTaskDto)
                .map(TaskCoordinator::mapToDomain)
                .flatMap(taskErrorResult -> {
                  if (taskErrorResult.isError()) {
                      Error error = taskErrorResult.getError();
                      return Result.error(new TaskTimeApi.ErrorDto(error.field(), error.message()));
                  }
                  taskRepository.updateTask(taskErrorResult.getSuccess());
                  return Result.success();
                });
    }


    private static Result<Task, Error> mapToDomain(TaskTimeApi.TaskDto updatedTaskDto) {
        return Task.create(
                updatedTaskDto.id(),
                updatedTaskDto.title(),
                updatedTaskDto.description(),
                TaskStatus.valueOf(updatedTaskDto.status().name()));
    }
}
