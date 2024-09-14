package com.example.demo.Organization;

import com.example.demo.Organization.Organization;
import com.example.demo.Organization.OrganizationService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/create")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization savedOrganization = organizationService.createOrganization(organization);
        return ResponseEntity.ok(savedOrganization);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        Optional<Organization> organization = organizationService.getOrganizationById(id);

        if (organization.isPresent()) {
            return ResponseEntity.ok(organization.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @PutMapping("/{id}/logo")
    public ResponseEntity<Organization> attachLogoToOrganization(@PathVariable Long id, @RequestBody Organization logoRequest) {
        Organization updatedOrganization = organizationService.updateOrganizationLogo(id, logoRequest.getLogo());
        return ResponseEntity.ok(updatedOrganization);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Organization> updateApprovalStatus(@PathVariable Long id, @RequestBody Organization statusRequest) {
        Organization updatedOrganization = organizationService.updateApprovalStatus(id, statusRequest.getApprovalStatus());
        return ResponseEntity.ok(updatedOrganization);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        boolean isDeleted = organizationService.deleteOrganization(id);
        if (isDeleted) {
            return ResponseEntity.ok("Organization deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
