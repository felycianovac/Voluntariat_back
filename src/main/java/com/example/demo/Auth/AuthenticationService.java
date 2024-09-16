package com.example.demo.Auth;

import com.example.demo.Email.EmailService;
import com.example.demo.Security.JwtService;
import com.example.demo.User.Roles;
import com.example.demo.User.UserDTO;
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
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request, HttpServletResponse response) {
        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            if (userOptional.get().isEnabled()) {
                return new RegisterResponse("Email already registered.", "PENDING_EMAIL_CONFIRMATION");
            }

            String token = generateVerificationToken();
            setTokenInCookie(response, userOptional.get().getEmail(), token);
            emailService.sendVerificationToken(userOptional.get().getEmail(), token);

            return new RegisterResponse("Success. Verification token sent via email.", "PENDING_EMAIL_CONFIRMATION");
        }

        Users user = Users.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Roles.user)
                .isProfileCompleted(false)
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(new Date())
                .isFirstLogin(true)
                .build();

        userRepository.save(user);

        String token = generateVerificationToken();
        setTokenInCookie(response, user.getEmail(), token);
        emailService.sendVerificationToken(user.getEmail(), token);

        return new RegisterResponse("Success. Verification token sent via email.", "PENDING_EMAIL_CONFIRMATION");
    }

    public RegisterResponse confirmEmail(String token, HttpServletRequest request) {
        if (token == null || token.isEmpty()) {
            return (new RegisterResponse("Missing token.", "ERROR"));
        }

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("verification_token".equals(cookie.getName())) {
                    String storedToken = cookie.getValue();
                    System.out.println("Stored token: " + storedToken);
                    System.out.println("Token: " + token);

                    if (storedToken.equals(token)) {
                        Optional<Users> userOptional = userRepository.findByEmail(extractUserEmailFromRequest(request));
                        System.out.println("User email: " + extractUserEmailFromRequest(request));

                        if (userOptional.isPresent()) {
                            Users user = userOptional.get();

                            if (user.isEnabled()) {
                                return (new RegisterResponse("Account already verified.", "ERROR"));
                            }

                            user.setProfileCompleted(true);
                            userRepository.save(user);

                            return (new RegisterResponse("Account verified.", "SUCCESS"));
                        } else {
                            return (new RegisterResponse("User not found.", "ERROR"));
                        }
                    } else {
                        return (new RegisterResponse("Invalid token.", "ERROR"));
                    }
                }
            }
        }

        return (new RegisterResponse("Token not found in cookies.", "ERROR"));
    }

    private String generateVerificationToken() {

        String token = UUID.randomUUID().toString();
        System.out.println("Token: " + token);

        return token;
    }

    private void setTokenInCookie(HttpServletResponse response,String email, String token) {
        ResponseCookie tokenCookie = ResponseCookie.from("verification_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24) // 1 day expiration
                .build();
        ResponseCookie emailCookie = ResponseCookie.from("user_email", email)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, emailCookie.toString());
    }

    private String extractUserEmailFromRequest(HttpServletRequest request) {
        Cookie cookie[] = request.getCookies();
        for(Cookie c : cookie){
            if(c.getName().equals("user_email")){
                return c.getValue();
            }
        }
        return null;

    }

    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new LoginResponse("User not found", null);
        }

        Users user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return new LoginResponse("Invalid credentials", null);
        }

        if (!user.isEnabled()) {
            return new LoginResponse("Account not verified", null);
        }

        String jwtToken = jwtService.generateToken(user);
        setJwtTokenInCookie(response, jwtToken);

        UserDTO userDTO = UserDTO.fromEntity(user);

        if (user.isFirstLogin()) {
            user.setFirstLogin(false);
            userRepository.save(user);
        }

        return new LoginResponse("Login successful", userDTO);
    }

    private void setJwtTokenInCookie(HttpServletResponse response, String jwtToken) {
        ResponseCookie tokenCookie = ResponseCookie.from("auth_token", jwtToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
    }
}

