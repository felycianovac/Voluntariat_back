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

    @PutMapping("/{id}/logo")
    public ResponseEntity<OrganizationDTO> updateOrganizationLogo(
            @PathVariable int id,
            @RequestBody OrganizationLogoRequest logoRequest) {

        OrganizationDTO organization = organizationService.updateOrganizationLogo(id, logoRequest.getLogo());
        return ResponseEntity.ok(organization);
    }

    @GetMapping()
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
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
