package com.example.demo.Auth;

import com.example.demo.User.Roles;
import com.example.demo.User.Users;
import com.example.demo.User.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth2 user request: " );
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        System.out.println("OAuth2 user email: " + oAuth2User.getAttribute("email"));

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        System.out.println(email);
        System.out.println(firstName);
        System.out.println(lastName);
        Optional<Users> userOptional = usersRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            Users newUser = Users.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .role(Roles.user)
                    .isProfileCompleted(true)
                    .isFirstLogin(true)
                    .build();

            usersRepository.save(newUser);
        } else {
            Users existingUser = userOptional.get();
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setFirstLogin(false);
            usersRepository.save(existingUser);
        }

        return oAuth2User;
    }
}
