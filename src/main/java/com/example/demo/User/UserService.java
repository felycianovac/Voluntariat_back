package com.example.demo.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@Builder
public class UserService {



    @Autowired
    private UsersRepository userRepository;

    public Users findByEmail(String email) {
        System.out.println(email);
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
