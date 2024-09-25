package com.example.demo.Opportunities;

import com.example.demo.Category.CategoriesDTO;
import com.example.demo.Organization.ApprovalStatus;
import com.example.demo.Organization.OrganizationDTO3;
import com.example.demo.Region.RegionsDTO;
import com.example.demo.Sessions.SessionsDTO;
import com.example.demo.Skills.SkillsDTO;
import com.example.demo.User.UserDTO3;
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
public class OpportunityDTO2 {
    private int id;
    private OrganizationDTO3 organization;
    private UserDTO3 createdBy;
    private String title;
    private String description;
    private String image;
    private RegionsDTO region;
    private String address;
    private boolean isHighPriority;
    private List<SessionsDTO> sessions;
    private List<CategoriesDTO> categories;
    private List<SkillsDTO> skills;
    private Date createdAt;
    private Date updatedAt;
    private ApprovalStatus approvalStatus;
    private Date approvalDate;


    public static OpportunityDTO2 fromEntity(Opportunities opportunity) {
        return OpportunityDTO2.builder()
                .id(opportunity.getId())
                .organization(OrganizationDTO3.fromEntity(opportunity.getOrganization()))
                .createdBy(UserDTO3.fromEntity(opportunity.getCreatedByUser()))
                .title(opportunity.getTitle())
                .description(opportunity.getDescription())
                .image(opportunity.getImage())
                .region(RegionsDTO.fromEntity(opportunity.getRegion()))
                .address(opportunity.getAddress())
                .isHighPriority(opportunity.isHighPriority())
                .sessions(opportunity.getSessions().stream()
                        .map(SessionsDTO::fromEntity)
                        .collect(Collectors.toList()))
                .categories(opportunity.getOpportunityCategories().stream()
                        .map(opportunityCategory -> CategoriesDTO.fromEntity(opportunityCategory.getCategory()))
                        .collect(Collectors.toList()))
                .skills(opportunity.getOpportunitySkills().stream()
                        .map(opportunitySkill -> SkillsDTO.fromEntity(opportunitySkill.getSkill()))
                        .collect(Collectors.toList()))
                .createdAt(opportunity.getCreatedAt())
                .updatedAt(opportunity.getUpdatedAt())
                .approvalStatus(opportunity.getApprovalStatus())
                .approvalDate(opportunity.getApprovalDate())
                .build();
    }
}