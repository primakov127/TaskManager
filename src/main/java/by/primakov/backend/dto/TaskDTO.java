package by.primakov.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TaskDTO {
    private long id;
    private String text;
}
