package io.github.clarenced.tasktime.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskTimeApi {

    private final List<TaskDto> tasks = getTasks();

    public record TaskDto(Long id, String title, String description){}
    public record CreateTaskDto(String title, String description){}

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        return ResponseEntity.ok(this.tasks);
    }

    @GetMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int taskId){
       return this.tasks.stream()
               .filter(task -> task.id() == taskId)
               .findFirst()
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTask(@RequestBody CreateTaskDto createTaskDto){
        var taskId = (long) this.tasks.size() + 1;
        var task = new TaskDto(taskId, createTaskDto.title(), createTaskDto.description());
        this.tasks.add(task);
        return ResponseEntity.status(201).build();
    }

    private static List<TaskDto> getTasks() {
        List<TaskDto> tasks = new ArrayList<>();
        tasks.add(new TaskDto(1L,"Prepare slides for the Spring meetup", "Prepare slides for the Spring meetup"));
        tasks.add(new TaskDto(3L, "Go to the theater", "Go to the theater"));
        return tasks;
    }
}
