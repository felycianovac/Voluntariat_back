package com.example.demo.Organization;


import com.example.demo.User.Users;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin
@AllArgsConstructor

public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/create")
    public ResponseEntity<OrganizationResponse> createOrganization(
            @RequestBody OrganizationRequest organizationRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(400).build();
        }
        Users authenticatedUser = (Users) authentication.getPrincipal();
        OrganizationResponse response = organizationService.createOrganization(organizationRequest, authenticatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @PutMapping("/{id}/logo")
    public ResponseEntity<Organization> updateOrganizationLogo(
            @PathVariable int id,
            @RequestBody OrganizationLogoRequest logoRequest) {

        Organization updatedOrganization = organizationService.updateOrganizationLogo(id, logoRequest.getLogo());
        return ResponseEntity.ok(updatedOrganization);
    }

//
//    @GetMapping("/{id}")
//    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
//        Optional<Organization> organization = organizationService.getOrganizationById(id);
//
//        if (organization.isPresent()) {
//            return ResponseEntity.ok(organization.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
//        boolean isDeleted = organizationService.deleteOrganization(id);
//        if (isDeleted) {
//            return ResponseEntity.ok("Organization deleted successfully");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
