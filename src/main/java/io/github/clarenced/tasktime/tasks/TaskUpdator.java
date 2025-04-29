package io.github.clarenced.tasktime.tasks;

public class TaskUpdator {


    static TaskTimeApi.TaskDto updateTask(TaskTimeApi.TaskDto originalTask, TaskTimeApi.UpdateTaskDto updateTaskDto) {
        return new TaskTimeApi.TaskDto(originalTask.id(),
                updateTaskDto.title().orElse(originalTask.title()),
                updateTaskDto.description().orElse(originalTask.description()),
                updateTaskDto.status().orElse(originalTask.status()));


    }
}