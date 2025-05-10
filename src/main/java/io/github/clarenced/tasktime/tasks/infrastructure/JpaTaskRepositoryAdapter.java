package io.github.clarenced.tasktime.tasks.infrastructure;

import io.github.clarenced.tasktime.tasks.domain.Task;
import io.github.clarenced.tasktime.tasks.domain.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaTaskRepositoryAdapter implements TaskRepository {

    private final JpaTaskRepository jpaTaskRepository;

    public JpaTaskRepositoryAdapter(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public void createTask(Task task) {
        TaskJpa taskJpa = new TaskJpa(task.getTitle(), task.getDescription());
        jpaTaskRepository.save(taskJpa);
    }

    @Override
    public List<Task> getTasks() {
        return List.of();
    }

    @Override
    public void updateTask(Task updatedTask) {
        // Implementation to be added
    }

    @Override
    public Optional<Task> findTaskById(Long taskId) {
        return Optional.empty();
    }
}
