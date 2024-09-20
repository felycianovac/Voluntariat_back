package com.example.demo.Organization;


import com.example.demo.User.UserService;
import com.example.demo.User.Users;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin
@AllArgsConstructor

public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<OrganizationResponse> createOrganization(
            @RequestBody OrganizationRequest organizationRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;

        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            // If the principal is a String (typically the email or username)
            email = (String) principal;
        } else if (principal instanceof UserDetails) {
            // If the principal is a UserDetails object (e.g., Users entity)
            email = ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
        Users authenticatedUser = userService.findByEmail(email);  // Assuming userService to fetch user entity

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

    @PutMapping("/{id}/status")

    public ResponseEntity<OrganizationDTO> updateApprovalStatus(
            @PathVariable int id,
            @RequestBody OrganizationApprovalRequest approvalRequest) {

        OrganizationDTO organization = organizationService.updateApprovalStatus(id, approvalRequest.getStatus());
        return ResponseEntity.ok(organization);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable int id) {
        OrganizationDTO organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(organization);
    }

}
