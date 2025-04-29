package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;

import java.util.Optional;

public class TaskExistenceChecker {
    private TaskExistenceChecker() {
    }
    /**
     * Validates that the task exists in the repository.
     *
     * @param taskId The ID of the task to validate
     * @param retrievedTaskOptional The task retrieved from the repository
     * @return A Result containing either the validated TaskDto or an ErrorDto
     */
    static Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> validateExistingTask(Long taskId, Optional<TaskTimeApi.TaskDto> retrievedTaskOptional) {
        return retrievedTaskOptional
                .map(Result::<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto>success)
                .orElse(Result.error(ErrorFactory.taskNotFound(taskId)));
    }

}