package io.github.clarenced.tasktime.api;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskRepository {
    private final List<TaskTimeApi.TaskDto> tasks;

    public TaskRepository() {
        this.tasks = new ArrayList<>();
        tasks.add(new TaskTimeApi.TaskDto(1L, "Prepare slides for the Spring meetup", "Prepare slides for the Spring meetup"));
        tasks.add(new TaskTimeApi.TaskDto(3L, "Go to the theater", "Go to the theater"));

    }

    void createTask(TaskTimeApi.CreateTaskDto createTaskDto) {
        var taskId = (long) this.tasks.size() + 1;
        var task = new TaskTimeApi.TaskDto(taskId, createTaskDto.title(), createTaskDto.description());
        this.tasks.add(task);
    }

    public List<TaskTimeApi.TaskDto> getTasks() {
        return tasks;
    }

    boolean newTaskIsAdded(String testTask) {
        return getTasks().stream().anyMatch(task -> task.title().equals(testTask));
    }

    public boolean noNewTaskIsCreated() {
        return this.tasks.size() == 2;
    }
}