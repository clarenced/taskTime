package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;

public class TaskValidator {


    /**
     * Validates a task creation request.
     *
     * @param createTaskDto The task creation request to validate
     * @return A Result containing either a validated CreateTaskDto or an ErrorDto
     */
    public static Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> validateTask(TaskTimeApi.CreateTaskDto createTaskDto) {
        if (createTaskDto.title().isEmpty()) {
            return createTaskError("title is empty", "title");
        }
        if (createTaskDto.description().isEmpty()) {
            return createTaskError("description is empty", "description");
        }
        if(createTaskDto.title().length() <= 5) {
            return createTaskError("title must have at least 5 characters", "title");
        }
        if(createTaskDto.title().length() > 30) {
            return createTaskError("title has more than 30 characters", "title");
        }
        if(createTaskDto.description().length() > 300) {
            return createTaskError("description has more than 300 characters", "description");
        }
        if(createTaskDto.description().length() <= 5) {
            return createTaskError("description must have at least 5 characters", "description");
        }
        return Result.success(createTaskDto);
    }

    private static Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> createTaskError(String description, String field) {
        return Result.error(new TaskTimeApi.ErrorDto(field, description));
    }
}