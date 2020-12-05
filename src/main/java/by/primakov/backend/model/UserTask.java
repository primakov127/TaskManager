package by.primakov.backend.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_tasks")
@Data
public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "completed")
    private boolean completed;
}
