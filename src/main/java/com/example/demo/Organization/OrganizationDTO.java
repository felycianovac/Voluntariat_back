package com.example.demo.Organization;
import com.example.demo.Region.Regions;
import com.example.demo.Region.RegionsDTO;
import com.example.demo.User.ApprovedByDTO;
import com.example.demo.User.CreatedByDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {

    private int id;

    private CreatedByDTO createdBy;

    private String name;

    private String description;

    private String logo;

    private String address;

    private RegionsDTO region;

    private List<Integer> categories;

    private String website;

    private String phoneNumber;

    private Date createdAt;

    private Date updatedAt;

    private String approvalStatus;

    private ApprovedByDTO approvedBy;

    private Date approvalDate;

    public static OrganizationDTO fromEntity(Organization organization) {
        return OrganizationDTO.builder()
                .id(organization.getOrganizationId())
                .createdBy(organization.getCreatedBy() != null ?
                        CreatedByDTO.fromEntity(organization.getCreatedBy()) : null)
                .name(organization.getName())
                .description(organization.getDescription())
                .logo(organization.getLogo())
                .address(organization.getAddress())
                .region(RegionsDTO.fromEntity(organization.getRegion()))
                .categories(organization.getOrganizationCategories().stream()
                        .map(organizationCategory -> organizationCategory.getCategory().getCategoryId())
                        .collect(Collectors.toList()))
                .website(organization.getWebsite())
                .phoneNumber(organization.getPhoneNumber())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .approvalStatus(organization.getApprovalStatus().name())
                .approvedBy(organization.getApprovedBy() != null ?
                        ApprovedByDTO.fromEntity(organization.getApprovedBy()) : null)
                .approvalDate(organization.getApprovalDate())
                .build();
    }
}