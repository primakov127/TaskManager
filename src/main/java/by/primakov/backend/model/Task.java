package by.primakov.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task extends BaseEntity {

    @Column(name = "text")
    private String text;

    @Column(name = "completed")
    private boolean completed;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.LAZY)
    private List<User> users;
}
