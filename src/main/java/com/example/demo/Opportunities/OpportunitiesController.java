package com.example.demo.Opportunities;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    }}
