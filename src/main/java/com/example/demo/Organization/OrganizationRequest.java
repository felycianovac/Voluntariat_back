package com.example.demo.Organization;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequest {
    private String name;
    private String description;
    private String address;
    private int regionId;
    private List<Integer> categoryIds;
    private String website;
    private String phoneNumber;
}
