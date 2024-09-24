package com.example.demo.Organization;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO3 {
    private int id;
    private String name;

    public static OrganizationDTO3 fromEntity(Organization organization) {
        return OrganizationDTO3.builder()
                .id(organization.getOrganizationId())
                .name(organization.getName())
                .build();
    }


}
