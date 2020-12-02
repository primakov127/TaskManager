package by.primakov.backend.rest;

import by.primakov.backend.dto.MyTaskDTO;
import by.primakov.backend.model.Task;
import by.primakov.backend.model.User;
import by.primakov.backend.repository.TaskRepository;
import by.primakov.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/myTasks")
public class MyTaskController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public MyTaskController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/allMy")
    public List<MyTaskDTO> GetMyTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        List<MyTaskDTO> result = currentUser.getTasks().stream()
                .map(task -> new MyTaskDTO(task.getId(), task.getText(), task.isCompleted())).collect(Collectors.toList());

        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/allUser")
    public List<MyTaskDTO> GetUserTasks(@RequestParam long userId) {
        User user = userRepository.getOne(userId);
        List<MyTaskDTO> result = null;
        result = user.getTasks().stream()
                .map(task -> new MyTaskDTO(task.getId(), task.getText(), task.isCompleted())).collect(Collectors.toList());

        return result;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/addTask")
    public MyTaskDTO AddTask(@RequestBody MyTaskDTO newTask) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        Task task = taskRepository.getOne(newTask.getId());
        // Check if user has already had this task
        if (currentUser.getTasks().stream().noneMatch(tsk -> (tsk.getId().equals(task.getId())))) {
            currentUser.getTasks().add(task);
            userRepository.save(currentUser);
        }
        MyTaskDTO result = new MyTaskDTO(task.getId(), task.getText(), task.isCompleted());

        return result;
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/deleteTask")
    public MyTaskDTO DeleteTask(@RequestParam long taskId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        Task task = taskRepository.getOne(taskId);
        // Check if user has deleted task
        if (currentUser.getTasks().stream().anyMatch(tsk -> (tsk.getId().equals(task.getId())))) {
            currentUser.getTasks().remove(task);
            userRepository.save(currentUser);
        }
        MyTaskDTO result = new MyTaskDTO(task.getId(), task.getText(), task.isCompleted());

        return result;
    }
}
