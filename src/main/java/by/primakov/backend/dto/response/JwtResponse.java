package by.primakov.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class JwtResponse {
    private String token;
    //private String type = "Bearer";
    //private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
