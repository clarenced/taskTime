package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;

import java.util.Optional;

public class TaskUpdator {


    static Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> updateTask(TaskTimeApi.TaskDto originalTask, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        Optional<String> title = updateTaskDto.title();
        Optional<String> description = updateTaskDto.description();
        if(title.isPresent() && title.get().length() > 30) {
            return Result.error(new TaskTimeApi.ErrorDto("title", "title has more than 30 characters"));
        }
        if(title.isPresent() && title.get().length() <= 5) {
            return Result.error(new TaskTimeApi.ErrorDto("title", "title must have at least 5 characters"));
        }
        if(description.isPresent() && description.get().length() > 300) {
            return Result.error(new TaskTimeApi.ErrorDto("description", "description has more than 300 characters"));
        }

        return Result.success(new TaskTimeApi.TaskDto(originalTask.id(),
                updateTaskDto.title().orElse(originalTask.title()),
                updateTaskDto.description().orElse(originalTask.description()),
                updateTaskDto.status().orElse(originalTask.status())));


    }


}