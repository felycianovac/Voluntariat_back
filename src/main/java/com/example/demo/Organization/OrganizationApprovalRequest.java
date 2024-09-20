package com.example.demo.Organization;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OrganizationApprovalRequest {
    private String status;
}
