package com.example.demo.Organization;

import com.example.demo.Category.Categories;
import com.example.demo.Organization.OrganizationRepository;
import com.example.demo.Organization.Organization;
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

    public OrganizationResponse createOrganization(OrganizationRequest organizationRequest, Users user) {
        Organization organization = Organization.builder()
                .name(organizationRequest.getName())
                .description(organizationRequest.getDescription())
                .address(organizationRequest.getAddress())
                .regionId(organizationRequest.getRegionId())
                .website(organizationRequest.getWebsite())
                .phoneNumber(organizationRequest.getPhoneNumber())
                .createdBy(user)
                .organizationCategories(organizationRequest.getCategoryIds().stream().map(categoryId -> OrganizationCategories.builder().category(Categories.builder().categoryId(categoryId).build()).build()).collect(Collectors.toList()))
                .build();

        organizationRepository.save(organization);

        return OrganizationResponse.builder()
                .message("Organization created successfully.")
                .status("success")
                .build();
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization updateOrganizationLogo(int organizationId, String logoUrl) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);

        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            organization.setLogo(logoUrl); // Set the new logo URL
            return organizationRepository.save(organization); // Save the updated organization
        } else {
            throw new RuntimeException("Organization not found with ID: " + organizationId);
        }
    }
}
//    public Optional<Organization> getOrganizationById(Long id) {
//        return organizationRepository.findById(id);
//    }
//
//    public List<Organization> getAllOrganizations() {
//        return organizationRepository.findAll();
//    }
//
//    public Organization updateOrganizationLogo(Long id, String logo) {
//        Optional<Organization> organizationOptional = organizationRepository.findById(id);
//        if (organizationOptional.isPresent()) {
//            Organization organization = organizationOptional.get();
//            organization.setLogo(logo);
//            return organizationRepository.save(organization);
//        } else {
//            throw new RuntimeException("Organization not found");
//        }
//    }
//
//    public Organization updateApprovalStatus(Long id, String approvalStatus) {
//        Optional<Organization> organizationOptional = organizationRepository.findById(id);
//        if (organizationOptional.isPresent()) {
//            Organization organization = organizationOptional.get();
//            organization.setApprovalStatus(approvalStatus);
//            return organizationRepository.save(organization);
//        } else {
//            throw new RuntimeException("Organization not found");
//        }
//    }
//
//    public boolean deleteOrganization(Long id) {
//        if (organizationRepository.existsById(id)) {
//            organizationRepository.deleteById(id);
//            return true;
//        } else {
//            return false;
//        }
//    }


