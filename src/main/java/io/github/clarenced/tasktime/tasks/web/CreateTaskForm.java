package io.github.clarenced.tasktime.tasks.web;

import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;

public class CreateTaskForm {
    private String title = "";
    private String description = "";
    private TaskTimeApi.TaskStatus status = TaskTimeApi.TaskStatus.TO_DO;

    public CreateTaskForm() {}

    public CreateTaskForm(String title, String description, TaskTimeApi.TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskTimeApi.TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskTimeApi.TaskStatus status) {
        this.status = status;
    }

    public TaskTimeApi.CreateTaskDto toCreateTaskDto() {
        return new TaskTimeApi.CreateTaskDto(title, description);
    }
}