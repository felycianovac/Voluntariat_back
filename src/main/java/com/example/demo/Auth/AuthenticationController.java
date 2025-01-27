package com.example.demo.Auth;

import com.example.demo.Auth.Profile.ProfilePictureRequest;
import com.example.demo.Auth.Profile.ProfileRequest;
import com.example.demo.User.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.User.Roles.user;

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
        System.out.println("Admin login endpoint reached");

        return ResponseEntity.ok(authenticationService.loginAdmin(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(authenticationService.logout(response, request));
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
    public ResponseEntity<UserDTO4> getProfile(
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(authenticationService.getProfile(httpRequest));
    }

//    @PostMapping("/send-otp")
//    public ResponseEntity<String> sendOtp(@RequestParam String email) {
//        authenticationService.generateAndSendOtp(email);
//        return ResponseEntity.ok("OTP sent to your email.");
//    }

    @PostMapping("/validate-otp")
    public ResponseEntity<LoginResponse> validateOtp(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String otp
    ) {
        return ResponseEntity.ok(authenticationService.validateOtpAndLogin(request,response, otp));


//        return ResponseEntity.ok(new LoginResponse("Login successful", userDTO, false));
    }


    @PatchMapping("/mfa")
    public ResponseEntity<String> toggleMfa(@RequestParam boolean enable, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = authentication.getName();
        authenticationService.toggleMfa(email, enable);
        return ResponseEntity.ok("MFA " + (enable ? "enabled" : "disabled") + " successfully.");
    }




}
