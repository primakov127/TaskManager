package by.primakov.backend.rest;

import by.primakov.backend.dto.request.LoginRequest;
import by.primakov.backend.dto.request.SignupRequest;
import by.primakov.backend.dto.response.JwtResponse;
import by.primakov.backend.dto.response.MessageResponse;
import by.primakov.backend.model.User;
import by.primakov.backend.security.jwt.JwtTokenProvider;
import by.primakov.backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("ERROR: Invalid username or password!"));
        }

        User user = userService.findByUsername(username);
        String token = jwtTokenProvider.createToken(username, user.getRoles());

        List<String> roleNames = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
        JwtResponse response = new JwtResponse(token, user.getUsername(), user.getEmail(), roleNames);

        log.info("IN login - user with username: {} successfully logged in", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody SignupRequest signupRequest) {
        if (!userService.isUniqUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("ERROR: Username is already taken!"));
        }
        if (!userService.isUniqEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("ERROR: Email is already taken!"));
        }

        User newUser = new User();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setPassword(signupRequest.getPassword());

        userService.register(newUser);

        log.info("IN register - user with username: {} successfully registered", newUser.getUsername());
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
