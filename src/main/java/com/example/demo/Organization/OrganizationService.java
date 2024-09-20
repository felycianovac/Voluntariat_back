package com.example.demo.Organization;

import com.example.demo.Category.Categories;
import com.example.demo.Region.Regions;
import com.example.demo.Region.RegionsRepository;
import com.example.demo.User.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private RegionsRepository regionsRepository;

    @Transactional
    public OrganizationResponse createOrganization(OrganizationRequest organizationRequest, Users user) {

        Regions region = regionsRepository.findById(organizationRequest.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        Organization organization = Organization.builder()
                .name(organizationRequest.getName())
                .description(organizationRequest.getDescription())
                .address(organizationRequest.getAddress())
                .region(region)
                .website(organizationRequest.getWebsite())
                .phoneNumber(organizationRequest.getPhoneNumber())
                .createdBy(user)
                .createdAt(new java.util.Date())
                .approvalStatus(ApprovalStatus.pending)
                .organizationCategories(organizationRequest.getCategoryIds().stream().map(categoryId -> OrganizationCategories.builder().category(Categories.builder().categoryId(categoryId).build()).build()).collect(Collectors.toList()))
                .build();


        List<OrganizationCategories> organizationCategories = organizationRequest.getCategoryIds()
                .stream()
                .map(categoryId -> {
                    Categories category = Categories.builder().categoryId(categoryId).build();
                    return OrganizationCategories.builder()
                            .category(category)
                            .organization(organization)
                            .build();
                })
                .collect(Collectors.toList());

        organization.setOrganizationCategories(organizationCategories);

        organizationRepository.save(organization);

        return OrganizationResponse.builder()
                .message("Organization created successfully.")
                .status("success")
                .build();
    }

    public List<OrganizationDTO> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(OrganizationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public OrganizationDTO updateOrganizationLogo(int organizationId, String logoUrl) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            organization.setLogo(logoUrl);
            organizationRepository.save(organization);
            return OrganizationDTO.fromEntity(organization);
        } else {
            throw new RuntimeException("Organization not found");
        }
    }

    public OrganizationDTO updateApprovalStatus(int organizationId, String approvalStatus) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            organization.setApprovalStatus(ApprovalStatus.valueOf(approvalStatus));
            organizationRepository.save(organization);
            return OrganizationDTO.fromEntity(organization);
        } else {
            throw new RuntimeException("Organization not found");
        }
    }


    public OrganizationDTO getOrganizationById(int id) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(id);

        if (optionalOrganization.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }

        Organization organization = optionalOrganization.get();

        return OrganizationDTO.fromEntity(organization);
    }


}