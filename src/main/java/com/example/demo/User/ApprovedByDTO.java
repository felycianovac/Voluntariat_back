package com.example.demo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedByDTO {
    private int id;
    private String username;

    public static ApprovedByDTO fromEntity(Users user) {
        return ApprovedByDTO.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .build();
    }

}
