package by.primakov.backend.dto.request;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
public class LoginRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "Username should not be empty.")
    @Size(max = 100, min = 3, message = "Username should be from 3 to 12 characters long.")
    private String username;

    @NotEmpty(message = "Password should not be empty.")
    @Size(min = 3, max = 255, message = "Password should be at least 6 characters long.")
    private String password;
}
