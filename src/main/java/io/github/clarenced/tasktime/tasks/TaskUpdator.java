package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;

import java.util.Optional;

public class TaskUpdator {


    static Result<TaskTimeApi.TaskDto, TaskTimeApi.ErrorDto> updateTask(TaskTimeApi.TaskDto originalTask, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        Optional<String> title = updateTaskDto.title();
        if(title.isPresent() && title.get().length() > 30) {
            return Result.error(new TaskTimeApi.ErrorDto("title", "title has more than 30 characters"));
        }

        return Result.success(new TaskTimeApi.TaskDto(originalTask.id(),
                updateTaskDto.title().orElse(originalTask.title()),
                updateTaskDto.description().orElse(originalTask.description()),
                updateTaskDto.status().orElse(originalTask.status())));


    }


}