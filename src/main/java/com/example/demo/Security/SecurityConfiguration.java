package com.example.demo.Security;

import com.example.demo.Auth.CustomOAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Autowired
    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;


    @Bean
    public static PasswordEncoder myPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails user = User.withUsername("user")
                .password(myPasswordEncoder().encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .cors(cors -> cors.disable())
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/api/login/oauth2").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth").authenticated()
//                        .requestMatchers("/api/login/oauth2").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/confirm-email").permitAll()
                        .requestMatchers("/api/auth/login/admin").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/profile").authenticated()
                        .requestMatchers("/api/auth/profile/profilePicture").authenticated()
                        .requestMatchers("api/organizations/*/logo").authenticated()
                        .requestMatchers("api/organizations/create").authenticated()
                        .requestMatchers("api/organizations").permitAll()
                        .requestMatchers("api/opportunities/create").authenticated()
                        .requestMatchers("api/opportunities/*/image").authenticated()
                        .requestMatchers("api/opportunities").permitAll()
                        .requestMatchers("api/opportunities/*/status").hasAnyAuthority("ADMIN")
                        .requestMatchers("api/opportunities/*").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/organizations/*/status").hasAnyAuthority("ADMIN")
                        .requestMatchers("api/organizations/*").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/regions").permitAll()
                        .requestMatchers("/api/skills").permitAll()
                        .requestMatchers("/api/categories").permitAll()
                        .requestMatchers("api/opportunities/organization/*").permitAll()
                        .requestMatchers("api/auth/validate-otp").permitAll()
                        .requestMatchers("/api/applications/*").authenticated()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                                .successHandler(customOAuthSuccessHandler))
//                        .clientRegistrationRepository(clientRegistrationRepository))

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }
    private Connector createStandardConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(8080);
        connector.setRedirectPort(8443);
        return connector;
    }
}