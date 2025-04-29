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
            return Result.error(ErrorFactory.titleIsEmpty());
        }
        if (createTaskDto.description().isEmpty()) {
            return Result.error(ErrorFactory.descriptionIsEmpty());
        }
        if(createTaskDto.title().length() <= 5) {
            return Result.error(ErrorFactory.titleHasLessThan5Characters());
        }
        if(createTaskDto.title().length() > 30) {
            return Result.error(ErrorFactory.titleHasMoreThan30Characters());
        }
        if(createTaskDto.description().length() > 300) {
            return Result.error(ErrorFactory.descriptionHasMore300Characters());
        }
        if(createTaskDto.description().length() <= 5) {
            return Result.error(ErrorFactory.descriptionHasLessThan5Characters());
        }
        return Result.success(createTaskDto);
    }

}