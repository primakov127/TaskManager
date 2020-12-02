package by.primakov.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MyTaskDTO {
    private long id;
    private String text;
    private boolean completed;
}
