package by.primakov.backend.dto.request;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
public class SignupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "Username should not be empty.")
    @Size(max = 100, min = 3, message = "Username should be from 3 to 100 characters long.")
    private String username;

    @NotEmpty(message = "First Name should not be empty.")
    @Size(max = 100, min = 3, message = "First Name should be from 3 to 100 characters long.")
    private String firstName;

    @NotEmpty(message = "Last Name should not be empty.")
    @Size(max = 100, min = 3, message = "First Name should be from 3 to 100 characters long.")
    private String lastName;

    @NotEmpty(message = "Email should not be empty.")
    @Email(message = "Invalid Email")
    private String email;

    @NotEmpty(message = "Password should not be empty.")
    @Size(min = 6, max = 255, message = "Password should be from 6 to 255 characters long.")
    private String password;
}
