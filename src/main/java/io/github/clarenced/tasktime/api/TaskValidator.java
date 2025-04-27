package io.github.clarenced.tasktime.api;

public class TaskValidator {


    /**
     * Validates a task creation request.
     *
     * @param createTaskDto The task creation request to validate
     * @return A Result containing either a validated CreateTaskDto or an ErrorDto
     */
    public static Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> validateTask(TaskTimeApi.CreateTaskDto createTaskDto) {
        if (createTaskDto.title().isEmpty()) {
            return Result.error(new TaskTimeApi.ErrorDto("title", "title is empty"));
        }
        if (createTaskDto.description().isEmpty()) {
            return Result.error(new TaskTimeApi.ErrorDto("description", "description is empty"));
        }
        if(createTaskDto.title().length() <= 5) {
            return Result.error(new TaskTimeApi.ErrorDto("title", "title must have at least 5 characters"));
        }
        if(createTaskDto.title().length() > 30) {
            return Result.error(new TaskTimeApi.ErrorDto("title", "title has more than 30 characters"));
        }
        return Result.success(createTaskDto);
    }
}