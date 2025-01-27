package com.example.demo.Auth;

import com.example.demo.Security.JwtService;
import com.example.demo.User.Roles;
import com.example.demo.User.Users;
import com.example.demo.User.UsersRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsersRepository usersRepository;  // Inject the repository to save user details

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();


        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        Optional<Users> existingUser = usersRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            Users newUser = Users.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .role(Roles.user)
                    .isProfileCompleted(false)
                    .isFirstLogin(true)
                    .build();
            usersRepository.save(newUser);
        } else {
            Users user = existingUser.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            if(user.isFirstLogin())
                user.setFirstLogin(false);
            usersRepository.save(user);
        }
        String jwtToken = jwtService.generateToken(oAuth2User);

        ResponseCookie cookie = ResponseCookie.from("auth_token", jwtToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // Required for cross-origin cookies
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

//        response.sendRedirect("https://27da7eb975cb1f.lhr.life");
    }

}
