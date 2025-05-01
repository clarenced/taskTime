package io.github.clarenced.tasktime.tasks.application;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.api.ErrorFactory;
import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;

import java.util.Optional;

public class TaskUpdator {


    public static Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> updateTask(TaskTimeApi.TaskDto originalTask, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        Optional<String> title = updateTaskDto.title();
        Optional<String> description = updateTaskDto.description();
        if(title.isPresent() && title.get().length() > 30) {
            return Result.error(ErrorFactory.titleHasMoreThan30Characters());
        }
        if(title.isPresent() && title.get().length() <= 5) {
            return Result.error(ErrorFactory.titleHasLessThan5Characters());
        }
        if(description.isPresent() && description.get().length() > 300) {
            return Result.error(ErrorFactory.descriptionHasMore300Characters());
        }
        if(description.isPresent() && description.get().length() <= 5) {
            return Result.error(ErrorFactory.descriptionHasLessThan5Characters());
        }

        return Result.success(new TaskTimeApi.TaskDto(originalTask.id(),
                updateTaskDto.title().orElse(originalTask.title()),
                updateTaskDto.description().orElse(originalTask.description()),
                updateTaskDto.status().orElse(originalTask.status())));


    }


}