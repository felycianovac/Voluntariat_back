package com.example.demo.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO3 {
    private int id;
    private String firstName;
    private String lastName;


    public static UserDTO3 fromEntity(Users user) {
        return UserDTO3.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
