package com.example.demo.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedByDTO {
    private int id;
    private String firstName;
    private String lastName;

    public static CreatedByDTO fromEntity(Users user) {
        return CreatedByDTO.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
