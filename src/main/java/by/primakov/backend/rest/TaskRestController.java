package by.primakov.backend.rest;

import by.primakov.backend.dto.TaskDTO;
import by.primakov.backend.dto.response.MessageResponse;
import by.primakov.backend.model.Task;
import by.primakov.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/task")
public class TaskRestController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskRestController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping(value = "/all")
    public List<TaskDTO> getTasks() {
        return taskRepository.findAll().stream()
                .map(e -> new TaskDTO(e.getId(), e.getText(), e.isCompleted())).collect(Collectors.toList());
    }

    @PostMapping(value = "/add")
    public ResponseEntity addTask(@RequestBody TaskDTO taskRequest) {

        taskRepository.save(new Task(taskRequest.getText()));
        return ResponseEntity.ok(new MessageResponse("Task added successfully!"));
    }
}
