package by.primakov.backend.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_tasks")
@Data
public class UserTask {

    public UserTask() {

    }

    public UserTask(User user, Task task, boolean completed) {
        this.user = user;
        this.task = task;
        this.completed = completed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "completed")
    private boolean completed;
}
