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
        return this.taskRepository.getTasks();
    }

    public Optional<TaskTimeApi.TaskDto> findTaskById(Long taskId) {
        return this.taskRepository.findTaskById(taskId);
    }

    public Result<Void, TaskTimeApi.ErrorDto> updateTask(Long taskId, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        return TaskExistenceChecker.validateExistingTask(taskId, findTaskById(taskId))
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
