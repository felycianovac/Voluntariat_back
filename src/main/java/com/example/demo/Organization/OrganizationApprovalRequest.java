package com.example.demo.Organization;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class OrganizationApprovalRequest {
    private String status;
}
