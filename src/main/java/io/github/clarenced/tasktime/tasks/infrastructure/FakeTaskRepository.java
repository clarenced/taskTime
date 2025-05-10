package io.github.clarenced.tasktime.tasks.infrastructure;

import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;
import io.github.clarenced.tasktime.tasks.domain.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class FakeTaskRepository implements TaskRepository {
    private final List<TaskTimeApi.TaskDto> tasks;

    public FakeTaskRepository() {
        this.tasks = new ArrayList<>();
        tasks.add(new TaskTimeApi.TaskDto(1L, "Prepare slides for the Spring meetup", "Prepare slides for the Spring meetup"));
        tasks.add(new TaskTimeApi.TaskDto(3L, "Go to the theater", "Go to the theater"));
    }

    @Override
    public void createTask(Task task) {
        var taskId = tasks.stream().map(TaskTimeApi.TaskDto::id).max(Comparator.naturalOrder()).orElse(0L) + 1;
        var taskDto = new TaskTimeApi.TaskDto(taskId, task.getTitle(), task.getDescription());
        this.tasks.add(taskDto);
    }

    @Override
    public List<TaskTimeApi.TaskDto> getTasks() {
        return tasks;
    }

    public boolean newTaskIsAdded(String testTask) {
        return getTasks().stream().anyMatch(task -> task.title().equals(testTask));
    }

    public boolean noNewTaskIsCreated() {
        return this.tasks.size() == 2;
    }

    @Override
    public void updateTask(TaskTimeApi.TaskDto updatedTask) {
        Optional<TaskTimeApi.TaskDto> taskDto = getTasks().stream()
                .filter(task -> task.id().equals(updatedTask.id()))
                .findFirst();

        taskDto.ifPresent(dto -> this.tasks.set(this.tasks.indexOf(dto), updatedTask));
        throw new IllegalArgumentException("Task with id " + updatedTask.id() + " does not exist");

    }

    public boolean assertThatTitle(Long taskId, String titleToBeUpdated) {
        return getTasks().stream()
                .filter(taskDto -> taskDto.id().equals(taskId))
                .findFirst()
                .map(taskDto -> taskDto.title().equals(titleToBeUpdated))
                .orElse(false);

    }

    public boolean assertThatDescription(long taskId, String descriptionToBeUpdated) {
        return getTasks().stream()
                .filter(taskDto -> taskDto.id().equals(taskId))
                .findFirst()
                .map(taskDto -> taskDto.description().equals(descriptionToBeUpdated))
                .orElse(false);
    }

    public boolean assertThatStatus(long taskId, TaskTimeApi.TaskStatus taskStatus) {
        return getTasks().stream()
                .filter(taskDto -> taskDto.id().equals(taskId))
                .findFirst()
                .map(taskDto -> taskDto.status().equals(taskStatus))
                .orElse(false);
    }

    @Override
    public Optional<TaskTimeApi.TaskDto> findTaskById(Long taskId) {
        return getTasks().stream()
                .filter(task -> task.id().equals(taskId))
                .findFirst();
    }
}
