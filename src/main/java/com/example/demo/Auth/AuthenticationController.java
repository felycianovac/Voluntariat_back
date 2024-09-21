package com.example.demo.Auth;

import com.example.demo.Auth.Profile.ProfilePictureRequest;
import com.example.demo.Auth.Profile.ProfileRequest;
import com.example.demo.User.UsersDTO2;
import com.example.demo.User.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            HttpServletResponse response,
            HttpServletRequest httpRequest

    ) {
        return ResponseEntity.ok(authenticationService.register(request, response, httpRequest));
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

    @PostMapping("/login/admin")
    public ResponseEntity<AdminResponse> loginAdmin(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        System.out.println("Admin login endpoint reached"); // Add logging

        return ResponseEntity.ok(authenticationService.loginAdmin(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.logout(response));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UsersDTO2> updateProfile(
            @RequestBody ProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(authenticationService.updateProfile(request, httpRequest));
    }




    @PutMapping("/profile/profilePicture")
    public ResponseEntity<UsersDTO2> updateProfilePicture(
            @RequestBody ProfilePictureRequest request,
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(authenticationService.updateProfilePicture(request, httpRequest));
    }

    @GetMapping()
    public ResponseEntity<UsersDTO2> getProfile(
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(authenticationService.getProfile(httpRequest));
    }




}
