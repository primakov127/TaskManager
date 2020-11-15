package by.primakov.backend.rest;

import by.primakov.backend.dto.request.LoginRequest;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
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
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
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
}
