package io.github.clarenced.tasktime.tasks.application;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;
import io.github.clarenced.tasktime.tasks.domain.Error;
import io.github.clarenced.tasktime.tasks.domain.Task;
import io.github.clarenced.tasktime.tasks.domain.TaskStatus;
import io.github.clarenced.tasktime.tasks.infrastructure.FakeTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaskCoordinator {

    private final FakeTaskRepository taskRepository;

    public TaskCoordinator(FakeTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Validate the task before creating it.
     * @param createTaskDto The task creation request
     * @return A Result indicating success or containing an error
     */
    public Result<Void, TaskTimeApi.ErrorDto> createTask(TaskTimeApi.CreateTaskDto createTaskDto) {
        Result<Task, io.github.clarenced.tasktime.tasks.domain.Error> taskResult = Task.create(createTaskDto.title(), createTaskDto.description(), TaskStatus.TO_DO);

        if (taskResult.isError()) {
            Error error = taskResult.getError();
            return Result.error(new TaskTimeApi.ErrorDto(error.field(), error.message()));
        }
        Task task = taskResult.getSuccess();
        taskRepository.createTask(task);
        return Result.success();
    }

    public List<TaskTimeApi.TaskDto> getTasks() {
        return this.taskRepository.getTasks().stream()
                .map(task -> new TaskTimeApi.TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        TaskTimeApi.TaskStatus.valueOf(task.getStatus().name())))
                .collect(Collectors.toList());
    }

    public Optional<TaskTimeApi.TaskDto> findTaskById(Long taskId) {
        return this.taskRepository.findTaskById(taskId)
                .map(task -> new TaskTimeApi.TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        TaskTimeApi.TaskStatus.valueOf(task.getStatus().name())));
    }

    public Result<Void, TaskTimeApi.ErrorDto> updateTask(Long taskId, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        return TaskExistenceChecker.validateExistingTask(taskId, findTaskById(taskId))
                .flatMap(existingTaskDto -> performTaskUpdate(existingTaskDto, updateTaskDto));
    }

    private Result<Void, TaskTimeApi.ErrorDto> performTaskUpdate(TaskTimeApi.TaskDto existingTaskDto, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        System.out.println("[DEBUG_LOG] Updating task: " + existingTaskDto);
        System.out.println("[DEBUG_LOG] With updates: " + updateTaskDto);

        return TaskUpdator.updateTask(existingTaskDto, updateTaskDto)
                .map(updatedTaskDto -> {
                    System.out.println("[DEBUG_LOG] Updated TaskDto: " + updatedTaskDto);

                    // Convert TaskDto to Task, preserving the id
                    Result<Task, Error> taskResult = Task.create(
                            updatedTaskDto.id(),
                            updatedTaskDto.title(),
                            updatedTaskDto.description(),
                            TaskStatus.valueOf(updatedTaskDto.status().name()));

                    System.out.println("[DEBUG_LOG] Task creation result: " + taskResult);

                    if (taskResult.isSuccess()) {
                        Task task = taskResult.getSuccess();
                        System.out.println("[DEBUG_LOG] Updating task in repository: " + task);
                        taskRepository.updateTask(task);

                        // Verify the task was updated
                        Optional<Task> updatedTask = taskRepository.findTaskById(task.getId());
                        System.out.println("[DEBUG_LOG] Task after update: " + updatedTask.orElse(null));
                    }
                    return null;
                });
    }
}
