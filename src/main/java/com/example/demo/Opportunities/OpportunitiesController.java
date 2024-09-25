package com.example.demo.Opportunities;


import com.example.demo.Organization.OrganizationApprovalRequest;
import com.example.demo.Organization.OrganizationDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@CrossOrigin
@AllArgsConstructor
public class OpportunitiesController {

    private final OpportunitiesService opportunitiesService;

    @PostMapping("/create")
    public ResponseEntity<OpportunityResponse> createOpportunity(
            @RequestBody OpportunityRequest request,
            HttpServletRequest httpRequest) {

        OpportunityResponse response = opportunitiesService.createOpportunity(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<OpportunityDTO> uploadOpportunityImage(
            @PathVariable int id,
            @RequestBody OpportunitiesImageRequest request) {
        OpportunityDTO updatedOpportunityDTO = opportunitiesService.attachImage(id, request);
        return ResponseEntity.ok(updatedOpportunityDTO);

    }


    @GetMapping()
    public ResponseEntity<List<?>> getAllOpportunities(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        List<?> opportunities = opportunitiesService.getAllOpportunities(isAdmin);


        return ResponseEntity.ok(opportunities);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<OpportunityDTO> updateApprovalStatus(
            @PathVariable int id,
            HttpServletRequest httpRequest,
            @RequestBody OpportunityApprovalRequest approvalRequest) {

        OpportunityDTO opportunityDTO = opportunitiesService.updateApprovalStatus(id,httpRequest,approvalRequest.getApprovalStatus());
        return ResponseEntity.ok(opportunityDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpportunityDTO> getOpportunityById(@PathVariable int id) {
        OpportunityDTO opportunityDTO = opportunitiesService.getOpportunityById(id);
        return ResponseEntity.ok(opportunityDTO);
    }



}
