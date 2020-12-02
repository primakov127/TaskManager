package by.primakov.backend.rest;

import by.primakov.backend.dto.TaskDTO;
import by.primakov.backend.model.Task;
import by.primakov.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/all")
    public List<TaskDTO> GetAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskDTO(task.getId(), task.getText())).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add")
    public TaskDTO AddTask(@RequestBody TaskDTO newTask) {
        Task result = taskRepository.save(new Task(newTask.getText()));
        return new TaskDTO(result.getId(), result.getText());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/update")
    public TaskDTO UpdateTask(@RequestBody TaskDTO updatedTask) {
        Task result = taskRepository.findById(updatedTask.getId()).get();
        if (result != null) {
            result.setText(updatedTask.getText());
            taskRepository.save(result);
        }
        return new TaskDTO(result.getId(), result.getText());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/delete")
    public TaskDTO DeleteTask(@RequestParam long id) {
        Task result = taskRepository.findById(id).get();
        if (result != null) {
            taskRepository.delete(result);
        }
        return new TaskDTO(result.getId(), result.getText());
    }
}
