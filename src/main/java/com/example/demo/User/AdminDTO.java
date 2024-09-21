package com.example.demo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private int id;
    private String email;
    private Date createdAt;


    public static AdminDTO fromEntity(Users admin) {
        return AdminDTO.builder()
                .id(admin.getUserId())
                .email(admin.getEmail())
                .createdAt(admin.getCreatedAt())
                .build();
    }
}
