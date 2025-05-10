package io.github.clarenced.tasktime.tasks.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository extends JpaRepository<TaskJpa, Long> {
}
