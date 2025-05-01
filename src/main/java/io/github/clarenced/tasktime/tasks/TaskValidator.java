package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;

public class TaskValidator {


    public static final int TITLE_MINIMAL_LENGTH = 5;
    public static final int TITLE_MAXIMUM_LENGTH = 30;
    public static final int DESCRIPTION_MAXIMUM_LENGTH = 300;
    public static final int DESCRIPTION_MINIMUM_LENGTH = 5;

    /**
     * Validates a task creation request.
     *
     * @param createTaskDto The task creation request to validateŒ
     * @return A Result containing either a validated CreateTaskDto or an ErrorDto
     */
    public static Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> validateTask(TaskTimeApi.CreateTaskDto createTaskDto) {
        if (createTaskDto.title().isEmpty()) {
            return Result.error(ErrorFactory.titleIsEmpty());
        }
        if (createTaskDto.description().isEmpty()) {
            return Result.error(ErrorFactory.descriptionIsEmpty());
        }
        if(createTaskDto.title().length() <= TITLE_MINIMAL_LENGTH) {
            return Result.error(ErrorFactory.titleHasLessThan5Characters());
        }
        if(createTaskDto.title().length() > TITLE_MAXIMUM_LENGTH) {
            return Result.error(ErrorFactory.titleHasMoreThan30Characters());
        }
        if(createTaskDto.description().length() > DESCRIPTION_MAXIMUM_LENGTH) {
            return Result.error(ErrorFactory.descriptionHasMore300Characters());
        }
        if(createTaskDto.description().length() <= DESCRIPTION_MINIMUM_LENGTH) {
            return Result.error(ErrorFactory.descriptionHasLessThan5Characters());
        }
        return Result.success(createTaskDto);
    }

}