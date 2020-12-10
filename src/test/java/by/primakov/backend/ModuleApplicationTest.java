package by.primakov.backend;

import by.primakov.backend.model.Task;
import by.primakov.backend.model.User;
import by.primakov.backend.repository.TaskRepository;
import by.primakov.backend.repository.UserRepository;
import by.primakov.backend.rest.TaskController;
import by.primakov.backend.service.implementation.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ModuleApplicationTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "max")
    public void getAllTasks() throws Exception {
        setUp();
        List<Task> tasks = Arrays.asList(
                new Task(1L ,"task 1"),
                new Task(2L ,"task 2"),
                new Task(3L ,"task 3")
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[*].id", Matchers.containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$[*].text", Matchers.containsInAnyOrder("task 1", "task 2", "task 3")));
    }

    @Test
    @WithMockUser(username = "max", roles = {"ADMIN"})
    public void getAllUsers() throws Exception {
        setUp();
        List<User> users = Arrays.asList(
                new User(1L ,"user 1"),
                new User(2L ,"user 2"),
                new User(3L ,"user 3")
        );

        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[*].id", Matchers.containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$[*].username", Matchers.containsInAnyOrder("user 1", "user 2", "user 3")));
    }

    @Test
    @WithMockUser(username = "max")
    public void searchTasks() throws Exception {
        setUp();
        List<Task> tasks = Arrays.asList(
                new Task(1L ,"task 1"),
                new Task(2L ,"task 2"),
                new Task(3L ,"task 3")
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/search").param("text", "task 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", Matchers.contains(1)))
                .andExpect(jsonPath("$[*].text", Matchers.contains("task 1")));
    }

    @Test
    public void getAllFromUserService() {
        List<User> users = Arrays.asList(
                new User(1L ,"user 1"),
                new User(2L ,"user 2"),
                new User(3L ,"user 3")
        );
        when(userRepository.findAll()).thenReturn(users);

        Assert.assertEquals(userService.getAll(), users);
    }
}
