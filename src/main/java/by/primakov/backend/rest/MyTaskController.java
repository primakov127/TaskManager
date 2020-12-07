package by.primakov.backend.rest;

import by.primakov.backend.dto.MyTaskDTO;
import by.primakov.backend.model.Task;
import by.primakov.backend.model.User;
import by.primakov.backend.model.UserTask;
import by.primakov.backend.repository.TaskRepository;
import by.primakov.backend.repository.UserRepository;
import by.primakov.backend.repository.UserTaskRepository;
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
    private final UserTaskRepository userTaskRepository;

    @Autowired
    public MyTaskController(UserRepository userRepository, TaskRepository taskRepository, UserTaskRepository userTaskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/allMy")
    public List<MyTaskDTO> GetMyTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        List<MyTaskDTO> result = currentUser.getUserTasks().stream()
                .map(userTask -> new MyTaskDTO(userTask.getTask().getId(), userTask.getTask().getText(), userTask.isCompleted()))
                .collect(Collectors.toList());

        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/allUser")
    public List<MyTaskDTO> GetUserTasks(@RequestParam long userId) throws Exception {
        try {
            User user = userRepository.getOne(userId);
            List<MyTaskDTO> result = null;
            result = user.getUserTasks().stream()
                    .map(userTask -> new MyTaskDTO(userTask.getTask().getId(), userTask.getTask().getText(), userTask.isCompleted()))
                    .collect(Collectors.toList());

            return result;
        } catch (Exception e) {
            throw new Exception("No user found with id: " + userId);
        }

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/addTask")
    public MyTaskDTO AddTask(@RequestBody MyTaskDTO newTask) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        Task task = taskRepository.getOne(newTask.getId());
        // Check if user has already had this task
        if (currentUser.getTasks().stream().noneMatch(tsk -> (tsk.getId().equals(task.getId())))) {
            userTaskRepository.save(new UserTask(currentUser, task, false));
        }
//        MyTaskDTO result = new MyTaskDTO(task.getId(), task.getText(), task.isCompleted());
        MyTaskDTO result = new MyTaskDTO(task.getId(), task.getText(), false);
        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/updateUserTask")
    public MyTaskDTO UpdateUserTask(@RequestParam long userId, @RequestParam long taskId, @RequestParam boolean completed) {
        User user = userRepository.getOne(userId);
        for (var task : user.getUserTasks()) {
            // Change delete from UserTaskRepository
            if (task.getTask().getId().equals(taskId)) {
                task.setCompleted(completed);
                userRepository.save(user);
                return new MyTaskDTO(task.getTask().getId(), task.getTask().getText(), task.isCompleted());
            }
        }

        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/deleteTask")
    public MyTaskDTO DeleteTask(@RequestParam long taskId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        Task task = taskRepository.getOne(taskId);
        // Check if user has deleted task
        for (var userTask : currentUser.getUserTasks()) {
            if (userTask.getUser().getId().equals(currentUser.getId()) && userTask.getTask().getId().equals(task.getId())) {
                userTaskRepository.delete(userTask);
            }
        }

        MyTaskDTO result = new MyTaskDTO(task.getId(), task.getText(), false);

        return result;
    }
}
