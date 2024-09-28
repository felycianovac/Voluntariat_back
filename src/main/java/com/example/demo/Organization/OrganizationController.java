package com.example.demo.Organization;


import com.example.demo.User.UserService;
import com.example.demo.User.Users;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
            email = (String) principal;
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
        Users authenticatedUser = userService.findByEmail(email);

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
    public ResponseEntity<List<?>> getAllOrganizations(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok(organizationService.getAllOrganizations(false));
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        System.out.println("isAdmin: " + isAdmin);
        List<?> organizations = organizationService.getAllOrganizations(isAdmin);


        return ResponseEntity.ok(organizations);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}/status")

    public ResponseEntity<OrganizationDTO> updateApprovalStatus(
            @PathVariable int id,
            HttpServletRequest httpRequest,
            @RequestBody OrganizationApprovalRequest approvalRequest) {

        OrganizationDTO organization = organizationService.updateApprovalStatus(id, httpRequest ,approvalRequest.getStatus());
        return ResponseEntity.ok(organization);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable int id) {
        OrganizationDTO organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(organization);
    }

}
