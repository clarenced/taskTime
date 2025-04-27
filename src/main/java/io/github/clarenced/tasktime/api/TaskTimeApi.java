package io.github.clarenced.tasktime.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskTimeApi {

    private final TaskRepository taskRepository = new TaskRepository();

    public record TaskDto(Long id, String title, String description){}
    public record CreateTaskDto(String title, String description){}

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        return ResponseEntity.ok(this.taskRepository.getTasks());
    }

    @GetMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int taskId){
       return this.taskRepository.getTasks().stream()
               .filter(task -> task.id() == taskId)
               .findFirst()
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTask(@RequestBody CreateTaskDto createTaskDto){
        taskRepository.createTask(createTaskDto);
        return ResponseEntity.status(201).build();
    }
}
