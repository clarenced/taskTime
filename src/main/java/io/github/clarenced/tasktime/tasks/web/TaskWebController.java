package io.github.clarenced.tasktime.tasks.web;

import io.github.clarenced.tasktime.tasks.api.TaskTimeApi;
import io.github.clarenced.tasktime.tasks.application.TaskCoordinator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskWebController {

    private final TaskCoordinator taskCoordinator;

    @Autowired
    public TaskWebController(TaskCoordinator taskCoordinator) {
        this.taskCoordinator = taskCoordinator;
    }

    @GetMapping
    public String listTasks(Model model) {
        List<TaskTimeApi.TaskDto> tasks = taskCoordinator.getTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new CreateTaskForm());
        return "tasks/index";
    }

    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        Optional<TaskTimeApi.TaskDto> task = taskCoordinator.findTaskById(id);
        if (task.isEmpty()) {
            return "redirect:/tasks?error=Task not found";
        }
        model.addAttribute("task", task.get());
        return "tasks/view";
    }

    @GetMapping("/new")
    public String newTaskForm(Model model) {
        model.addAttribute("task", new CreateTaskForm());
        model.addAttribute("statuses", TaskTimeApi.TaskStatus.values());
        return "tasks/form";
    }

    @PostMapping
    public String createTask(@ModelAttribute CreateTaskForm taskForm, Model model) {
        var createTaskDto = taskForm.toCreateTaskDto();
        var result = taskCoordinator.createTask(createTaskDto);
        if (result.isError()) {
            model.addAttribute("error", result.getError());
            model.addAttribute("task", taskForm);
            model.addAttribute("statuses", TaskTimeApi.TaskStatus.values());
            return "tasks/form";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model) {
        Optional<TaskTimeApi.TaskDto> task = taskCoordinator.findTaskById(id);
        if (task.isEmpty()) {
            return "redirect:/tasks?error=Task not found";
        }
        model.addAttribute("task", task.get());
        model.addAttribute("statuses", TaskTimeApi.TaskStatus.values());
        return "tasks/edit";
    }

    @PostMapping("/{id}")
    public String updateTask(@PathVariable Long id, 
                            @RequestParam Optional<String> title,
                            @RequestParam Optional<String> description,
                            @RequestParam Optional<TaskTimeApi.TaskStatus> status,
                            Model model) {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(title, description, status);
        var result = taskCoordinator.updateTask(id, updateTaskDto);
        
        if (result.isError()) {
            Optional<TaskTimeApi.TaskDto> task = taskCoordinator.findTaskById(id);
            model.addAttribute("task", task.orElse(null));
            model.addAttribute("error", result.getError());
            model.addAttribute("statuses", TaskTimeApi.TaskStatus.values());
            return "tasks/edit";
        }
        return "redirect:/tasks";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteTask(@PathVariable Long id) {
        // For HTMX delete requests
        taskCoordinator.deleteTask(id);
        return "";
    }

    // HTMX Endpoints for dynamic updates
    @GetMapping("/htmx/list")
    public String getTaskList(Model model) {
        List<TaskTimeApi.TaskDto> tasks = taskCoordinator.getTasks();
        model.addAttribute("tasks", tasks);
        return "tasks/fragments/task-list";
    }

    @PostMapping("/htmx/create")
    public String createTaskHtmx(@ModelAttribute CreateTaskForm taskForm, Model model) {
        var createTaskDto = taskForm.toCreateTaskDto();
        var result = taskCoordinator.createTask(createTaskDto);
        if (result.isError()) {
            model.addAttribute("error", result.getError());
            return "tasks/fragments/error";
        }
        
        // Return updated task list
        List<TaskTimeApi.TaskDto> tasks = taskCoordinator.getTasks();
        model.addAttribute("tasks", tasks);
        return "tasks/fragments/task-list";
    }

    @PutMapping("/htmx/{id}/status")
    public String updateTaskStatus(@PathVariable Long id, 
                                  @RequestParam TaskTimeApi.TaskStatus status,
                                  Model model) {
        var updateTaskDto = new TaskTimeApi.UpdateTaskDto(
            Optional.empty(), 
            Optional.empty(), 
            Optional.of(status)
        );
        
        var result = taskCoordinator.updateTask(id, updateTaskDto);
        if (result.isError()) {
            model.addAttribute("error", result.getError());
            return "tasks/fragments/error";
        }
        
        // Return updated task list
        List<TaskTimeApi.TaskDto> tasks = taskCoordinator.getTasks();
        model.addAttribute("tasks", tasks);
        return "tasks/fragments/task-list";
    }
}