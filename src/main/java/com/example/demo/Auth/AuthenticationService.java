package com.example.demo.Auth;

import com.example.demo.Email.EmailService;
import com.example.demo.User.Roles;
import com.example.demo.User.Users;
import com.example.demo.User.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Register the user and send an email with a verification token
    public RegisterResponse register(RegisterRequest request, HttpServletResponse response) {
        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            if (userOptional.get().isEnabled()) {
                return new RegisterResponse("Email already registered.", "PENDING_EMAIL_CONFIRMATION");
            }

            String token = generateVerificationToken();
            setTokenInCookie(response, token);
            emailService.sendVerificationToken(userOptional.get().getEmail(), token);

            return new RegisterResponse("Success. Verification token sent via email.", "PENDING_EMAIL_CONFIRMATION");
        }

        // Register new user
        Users user = Users.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Roles.user)
                .isProfileCompleted(false)
                .dateOfBirth(request.getDateOfBirth())
                .createdAt(new Date())
                .build();

        userRepository.save(user);

        // Generate verification token and send via email
        String token = generateVerificationToken();
        setTokenInCookie(response, token);
        emailService.sendVerificationToken(user.getEmail(), token);

        return new RegisterResponse("Success. Verification token sent via email.", "PENDING_EMAIL_CONFIRMATION");
    }

    // Verifies the email using the token from the cookie
    public ResponseEntity<String> verifyEmail(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("verification_token".equals(cookie.getName())) {
                    String token = cookie.getValue();

                    // Validate the token
                    if (isTokenValid(token)) {
                        // Extract user email from the request or cookies and mark the email as verified
                        Optional<Users> userOptional = userRepository.findByEmail(extractUserEmailFromRequest(request));
                        if (userOptional.isPresent()) {
                            Users user = userOptional.get();
                            user.setProfileCompleted(true); // Mark profile as completed
                            userRepository.save(user); // Save the user

                            return ResponseEntity.ok("Email verified successfully.");
                        } else {
                            return ResponseEntity.status(401).body("Invalid or expired token.");
                        }
                    } else {
                        return ResponseEntity.status(401).body("Invalid or expired token.");
                    }
                }
            }
        }
        return ResponseEntity.status(400).body("Verification token not found.");
    }

    // Helper methods
    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private boolean isTokenValid(String token) {
        return token != null && !token.isEmpty();
    }

    private void setTokenInCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("verification_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24) // 1 day expiration
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractUserEmailFromRequest(HttpServletRequest request) {
        // You might use the email from the request body, query parameter, or cookie
        // In this example, I'm just assuming it's from the request for simplicity
        return request.getParameter("email");
    }
}

