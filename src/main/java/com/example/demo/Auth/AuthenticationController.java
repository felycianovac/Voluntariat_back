package com.example.demo.Auth;

import com.example.demo.User.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private final AuthenticationService authenticationService;
    private final UsersRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response // Add HttpServletResponse as a parameter

    ) {
        return ResponseEntity.ok(authenticationService.register(request, response));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<RegisterResponse> confirm(
            @RequestParam String token,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(authenticationService.confirmEmail(token, request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.login(request, response));
    }



}
