package io.github.clarenced.tasktime.tasks.infrastructure;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.domain.Error;
import io.github.clarenced.tasktime.tasks.domain.Task;
import io.github.clarenced.tasktime.tasks.domain.TaskStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "use.real.database", havingValue = "true")
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
        return  jpaTaskRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTask(Task updatedTask) {
        // Implementation to be added
    }

    @Override
    public Optional<Task> findTaskById(Long taskId) {
        return jpaTaskRepository.findById(taskId).map(this::mapToDomain);
    }

    private Task mapToDomain(TaskJpa taskJpa) {
        return Task
                .create(taskJpa.getId(), taskJpa.getTitle(), taskJpa.getDescription(), TaskStatus.valueOf(taskJpa.getStatus().name()))
                .getSuccess();
    }
}
