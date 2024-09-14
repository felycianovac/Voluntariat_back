package com.example.demo.Organization;

import com.example.demo.Organization.OrganizationRepository;
import com.example.demo.Organization.Organization;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional
    public Organization createOrganization(Organization organizationRequest) {
        Organization organization = new Organization();
        organization.setName(organizationRequest.getName());
        organization.setDescription(organizationRequest.getDescription());
        organization.setAddress(organizationRequest.getAddress());
        organization.setRegion(organizationRequest.getRegion());
        organization.setCategories(organizationRequest.getCategories());
        organization.setWebsite(organizationRequest.getWebsite());
        organization.setPhoneNumber(organizationRequest.getPhoneNumber());
        organization.setLogo(organizationRequest.getLogo());
        organization.setApprovalStatus("in progress");

        return organizationRepository.save(organization);
    }
    //{
    //	"name": "org name",
    //	"description": "description",
    //	"address": "address",
    //	"region": 3,
    //    "categories": [
    //		1,
    //		4
    //	],
    //    "website": "url",
    //    "phoneNumber": "+37368576855",
    //    "logo": "logo-url"
    //
    //}

    public Optional<Organization> getOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization updateOrganizationLogo(Long id, String logo) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            organization.setLogo(logo);
            return organizationRepository.save(organization);
        } else {
            throw new RuntimeException("Organization not found");
        }
    }

    public Organization updateApprovalStatus(Long id, String approvalStatus) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            organization.setApprovalStatus(approvalStatus);
            return organizationRepository.save(organization);
        } else {
            throw new RuntimeException("Organization not found");
        }
    }

    public boolean deleteOrganization(Long id) {
        if (organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}


