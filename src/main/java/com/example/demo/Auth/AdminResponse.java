package com.example.demo.Auth;


import com.example.demo.User.AdminDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponse {
    private String message;
    private AdminDTO userInfo;
}
