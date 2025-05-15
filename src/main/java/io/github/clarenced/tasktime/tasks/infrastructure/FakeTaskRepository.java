package io.github.clarenced.tasktime.tasks.infrastructure;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.domain.Task;
import io.github.clarenced.tasktime.tasks.domain.TaskStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@ConditionalOnProperty(name = "use.real.database", havingValue = "false")
public class FakeTaskRepository implements TaskRepository {
    private final List<Task> tasks;

    public FakeTaskRepository() {
        this.tasks = new ArrayList<>();
        var task1 = Task.create(1L, "Prepare slides for the Spring meetup", "Prepare slides for the Spring meetup", TaskStatus.TO_DO);
        var task2 = Task.create(3L, "Go to the theater", "Go to the theater", TaskStatus.TO_DO);
        tasks.add(task1.getSuccess());
        tasks.add(task2.getSuccess());
    }

    @Override
    public void createTask(Task task) {
        this.tasks.add(task);
    }

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    public boolean newTaskIsAdded(String testTask) {
        return getTasks().stream().anyMatch(task -> task.getTitle().equals(testTask));
    }

    public boolean noNewTaskIsCreated() {
        // The initial repository has 2 tasks
        return this.tasks.size() == 2;
    }

    @Override
    public void updateTask(Task updatedTask) {
        Optional<Task> task = getTasks().stream()
                .filter(t -> t.getId().equals(updatedTask.getId()))
                .findFirst();

        if (task.isPresent()) {
            this.tasks.set(this.tasks.indexOf(task.get()), updatedTask);
        }
        // If the task doesn't exist, do nothing
    }

    public boolean assertThatTitle(Long taskId, String titleToBeUpdated) {
        return getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .map(task -> task.getTitle().equals(titleToBeUpdated))
                .orElse(false);
    }

    public boolean assertThatDescription(long taskId, String descriptionToBeUpdated) {
        return getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .map(task -> task.getDescription().equals(descriptionToBeUpdated))
                .orElse(false);
    }

    public boolean assertThatStatus(long taskId, io.github.clarenced.tasktime.tasks.api.TaskTimeApi.TaskStatus taskStatus) {
        return getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .map(task -> task.getStatus().name().equals(taskStatus.name()))
                .orElse(false);
    }

    @Override
    public Optional<Task> findTaskById(Long taskId) {
        return getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst();
    }
}
