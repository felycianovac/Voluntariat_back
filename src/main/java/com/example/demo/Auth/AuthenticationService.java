package com.example.demo.Auth;

import com.example.demo.Auth.Profile.ProfilePictureRequest;
import com.example.demo.Auth.Profile.ProfileRequest;
import com.example.demo.Email.EmailService;
import com.example.demo.Region.Regions;
import com.example.demo.Region.RegionsRepository;
import com.example.demo.Security.JwtService;
import com.example.demo.Skills.Skills;
import com.example.demo.Skills.SkillsRepository;
import com.example.demo.User.*;
import com.example.demo.VolunteerSkills.VolunteerSkills;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final RegionsRepository regionRepository;
    private final SkillsRepository skillsRepository;

    public RegisterResponse register(RegisterRequest request, HttpServletResponse response, HttpServletRequest httpRequest) {
        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());
        Optional<Users> userOptionalByPhoneNumber = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOptionalByPhoneNumber.isPresent()) {
            if(userOptionalByPhoneNumber.get().isEnabled()) {
                return new RegisterResponse("An account with this phone number already exists.", "PHONE_NUMBER_ALREADY_EXISTS");
            }
        }
        if (userOptional.isPresent()) {
            if (userOptional.get().isEnabled()) {
                return new RegisterResponse("An account with this email already exists.", "EMAIL_ALREADY_EXISTS");
            }
        }
        if((userOptionalByPhoneNumber.isPresent()  && !userOptionalByPhoneNumber.get().isEnabled())|| (userOptional.isPresent() && !userOptional.get().isEnabled())) {
            String existingToken = getTokenFromCookie(httpRequest);
            if (existingToken == null) {
                String token = generateVerificationToken();
                setTokenInCookie(response, userOptional.get().getEmail(), token);
                emailService.sendVerificationToken(userOptional.get().getEmail(), token);
                return new RegisterResponse("Verification token expired. A new token has been sent via email.", "NEW_TOKEN_SENT");
            } else {
                return new RegisterResponse("A verification token is already valid. Please check your email.", "PENDING_EMAIL_CONFIRMATION");
            }
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

        return new RegisterResponse("Success. Verification token sent via email.", "EMAIL_SENT");
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

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("verification_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
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

        if (!user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER"))) {
            return new LoginResponse("Access denied: insufficient permissions", null);
        }

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


    public AdminResponse loginAdmin(LoginRequest request, HttpServletResponse response) {
        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            return new AdminResponse("Admin not found", null);
        }

        Users user = userOptional.get();
        if (!user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            return new AdminResponse("Access denied: insufficient permissions", null);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return new AdminResponse("Invalid credentials", null);
        }

        String jwtToken = jwtService.generateToken(user);
        setJwtTokenInCookie(response, jwtToken);

        AdminDTO adminDTO = AdminDTO.fromEntity(user);

        return new AdminResponse("Login successful", adminDTO);
    }

    public LogoutResponse logout(HttpServletResponse response) {
        ResponseCookie tokenCookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());

        return new LogoutResponse("Logout successful");
    }

    public UsersDTO2 updateProfile(ProfileRequest request, HttpServletRequest httpRequest) {
        String email = request.getEmail();

        // Find the user in the database by email
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getRegion() != null) {
            Regions region = regionRepository.findById(request.getRegion().getId())
                    .orElseThrow(() -> new RuntimeException("Region not found"));
            user.setRegion(region);
        }
        if (request.getSkills() != null) {
            List<VolunteerSkills> newSkills = request.getSkills().stream()
                    .map(skillsDTO -> {
                        Skills skill = skillsRepository.findById(skillsDTO.getId())
                                .orElseThrow(() -> new RuntimeException("Skill not found"));
                        return VolunteerSkills.builder()
                                .volunteer(user)
                                .skill(skill)
                                .build();
                    })
                    .collect(Collectors.toList());

            // Clear the old skills and set new ones
            user.getVolunteerSkills().clear();
            user.getVolunteerSkills().addAll(newSkills);
        }

        // Save the updated user
        Users updatedUser = userRepository.save(user);

        // Return the updated user DTO
        return UsersDTO2.fromEntity(updatedUser);
    }

    public UsersDTO2 updateProfilePicture(ProfilePictureRequest request, HttpServletRequest httpRequest) {
        String email = httpRequest.getUserPrincipal().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        Users updatedUser = userRepository.save(user);

        return UsersDTO2.fromEntity(updatedUser);
    }

    public UsersDTO2 getProfile(HttpServletRequest httpRequest) {
        String email = httpRequest.getUserPrincipal().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UsersDTO2.fromEntity(user);
    }
}


