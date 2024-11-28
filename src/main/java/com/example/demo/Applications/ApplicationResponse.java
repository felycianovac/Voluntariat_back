package com.example.demo.Applications;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private String status;
    private String message;
}
