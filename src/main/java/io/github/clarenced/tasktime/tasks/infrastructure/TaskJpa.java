package io.github.clarenced.tasktime.tasks.infrastructure;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "task", schema = "taskTime")
public class TaskJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "description", length = 300, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatusJpa status;

    // Constructeurs
    protected TaskJpa() {
        // Pour JPA
    }

    public TaskJpa(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatusJpa.TO_DO;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatusJpa getStatus() {
        return status;
    }


    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatusJpa status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskJpa taskJpa = (TaskJpa) o;
        return Objects.equals(id, taskJpa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

