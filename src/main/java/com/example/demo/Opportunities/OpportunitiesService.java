package com.example.demo.Opportunities;


import com.example.demo.Category.Categories;
import com.example.demo.Category.CategoriesDTO;
import com.example.demo.Category.CategoriesRepository;
import com.example.demo.OpportunityCategories.OpportunitiesCategoryRepository;
import com.example.demo.OpportunityCategories.OpportunityCategories;
import com.example.demo.OpportunitySkills.OpportunitySkills;
import com.example.demo.OpportunitySkills.OpportunitySkillsRepository;
import com.example.demo.Organization.*;
import com.example.demo.Region.Regions;
import com.example.demo.Region.RegionsRepository;
import com.example.demo.Sessions.Sessions;
import com.example.demo.Sessions.SessionsRepository;
import com.example.demo.Skills.Skills;
import com.example.demo.Skills.SkillsRepository;
import com.example.demo.User.Users;
import com.example.demo.User.UsersRepository;
import com.example.demo.VolunteerSkills.VolunteerSkills;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpportunitiesService {
    @Autowired
    private OpportunitiesRepository opportunitiesRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private RegionsRepository regionsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private SessionsRepository sessionsRepository;
    @Autowired
    private SkillsRepository skillsRepository;
    @Autowired
    private OpportunitiesCategoryRepository opportunitiesCategoryRepository;
    @Autowired
    private OpportunitySkillsRepository opportunitySkillsRepository;





    public OpportunityResponse createOpportunity(OpportunityRequest request, HttpServletRequest httpRequest) {

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Regions region = regionsRepository.findById(request.getRegion().getId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        String email = httpRequest.getUserPrincipal().getName();
        Users createdByUser = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Opportunities opportunity = Opportunities.builder()
                .organization(organization)
                .createdByUser(createdByUser)
                .title(request.getTitle())
                .description(request.getDescription())
                .region(region)
                .approvalStatus(ApprovalStatus.pending)
                .address(request.getAddress())
                .isHighPriority(request.isHighPriority())
                .createdAt(new Date())
                .build();

        final Opportunities savedOpportunity = opportunitiesRepository.save(opportunity);

        List<Sessions> sessionsList = request.getSessions().stream()
                .map(sessionDTO -> Sessions.builder()
                        .opportunity(savedOpportunity)
//                        .sessionId(sessionDTO.getId()) //???????????
                        .sessionDate(sessionDTO.getDate())
                        .sessionStartTime(sessionDTO.getStartTime())
                        .sessionEndTime(sessionDTO.getEndTime())
                        .spotsLeft(sessionDTO.getSpotsLeft())
                        .build())
                .collect(Collectors.toList());

        sessionsRepository.saveAll(sessionsList);

        List<OpportunityCategories> opportunityCategories = request.getCategories().stream()
                .map(categoryId -> {
                    Categories category = categoriesRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found"));
                    return OpportunityCategories.builder()
                            .opportunity(savedOpportunity)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        opportunitiesCategoryRepository.saveAll(opportunityCategories);

        List<OpportunitySkills> opportunitySkillsList = request.getSkills().stream()
                .map(skillId -> {
                    Skills skill = skillsRepository.findById(skillId)
                            .orElseThrow(() -> new RuntimeException("Skill not found"));
                    return OpportunitySkills.builder()
                            .opportunity(savedOpportunity)
                            .skill(skill)
                            .build();
                })
                .collect(Collectors.toList());

        opportunitySkillsRepository.saveAll(opportunitySkillsList);

        return OpportunityResponse.builder()
                .message("Opportunity created successfully")
                .status("success")
                .build();
    }


    public OpportunityDTO attachImage(int id, OpportunitiesImageRequest request) {
        Opportunities opportunity = opportunitiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        opportunity.setImage(request.getImage());
        Opportunities updatedOpportunity = opportunitiesRepository.save(opportunity);
        return OpportunityDTO.fromEntity(updatedOpportunity);
    }

    public List<?> getAllOpportunities(boolean isAdmin) {
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();

        if (!isAdmin) {
            opportunitiesList = opportunitiesList.stream()
                    .filter(opp -> "APPROVED".equalsIgnoreCase(opp.getApprovalStatus().name()))
                    .collect(Collectors.toList());
        }

        return opportunitiesList.stream()
                .map(opp -> {
                    if (isAdmin) {
                        System.out.println("Admin is viewing opportunities.");
                        return OpportunityDTO.fromEntity(opp);
                    } else {
                        System.out.println("Non-admin is viewing opportunities.");
                        return OpportunityDTO2.fromEntity(opp);
                    }
                })
                .collect(Collectors.toList());
    }

    public OpportunityDTO updateApprovalStatus(int id,HttpServletRequest httpServletRequest, String approvalStatus) {
        Optional<Opportunities> opportunitiesOptional = opportunitiesRepository.findById(id);
        if (opportunitiesOptional.isPresent()) {
            Opportunities opportunities = opportunitiesOptional.get();
            opportunities.setApprovalStatus(ApprovalStatus.valueOf(approvalStatus));
            opportunities.setApprovedBy(usersRepository.findByEmail(httpServletRequest.getUserPrincipal().getName()).orElseThrow(() -> new RuntimeException("User not found")));
            opportunities.setApprovalDate(new Date());
            opportunitiesRepository.save(opportunities);
            return OpportunityDTO.fromEntity(opportunities);
        } else {
            throw new RuntimeException("Organization not found");
        }
    }


    public OpportunityDTO getOpportunityById(int id) {
        Opportunities opportunity = opportunitiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        return OpportunityDTO.fromEntity(opportunity);
    }

    public List<OpportunityDTO> getOpportunitiesByOrganizationId(int organizationId, Authentication authentication) {
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        String currentUserEmail = authentication != null ? authentication.getName() : null;

        List<Opportunities> opportunities;

        if (isAdmin) {
            opportunities = opportunitiesRepository.findByOrganization_OrganizationId(organizationId);
        } else if (currentUserEmail != null) {
            opportunities = opportunitiesRepository.findByOrganization_OrganizationId(organizationId).stream()
                    .filter(opportunity ->
                            opportunity.getApprovalStatus() == ApprovalStatus.approved ||
                                    opportunity.getCreatedByUser().getEmail().equals(currentUserEmail))
                    .collect(Collectors.toList());
        } else {
            opportunities = opportunitiesRepository.findByOrganization_OrganizationId(organizationId).stream()
                    .filter(opportunity -> opportunity.getApprovalStatus() == ApprovalStatus.approved)
                    .collect(Collectors.toList());
        }

        // Convert to DTO
        return opportunities.stream()
                .map(OpportunityDTO::fromEntity)
                .collect(Collectors.toList());
    }


    //TODO: modify date format
}
