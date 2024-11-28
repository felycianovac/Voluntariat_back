package com.example.demo.Applications;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
public class ApplicationsController {

    private final ApplicationService applicationsService;

    @PostMapping("/{opportunityId}")
    public ResponseEntity<ApplicationResponse> submitApplication(
            @PathVariable int opportunityId,
            @RequestBody ApplicationRequest request,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(new ApplicationResponse("error", "Unauthorized"));
        }

        String email = authentication.getName();
        ApplicationResponse response = applicationsService.submitApplication(opportunityId, request, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/opportunities/{oppId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByOpportunityId(@PathVariable int oppId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(applicationsService.getApplicationsByOpportunityId(oppId, currentUserEmail));
    }
}
