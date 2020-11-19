package by.primakov.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task extends BaseEntity {

    public Task() {
        super();
    }

    public Task(String text) {
        super(new Date(), new Date(), Status.ACTIVE);
        this.text = text;
        this.completed = false;
    }

    @Column(name = "text")
    private String text;

    @Column(name = "completed")
    private boolean completed;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.LAZY)
    private List<User> users;
}
