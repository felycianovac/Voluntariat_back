package com.example.demo.Opportunities;

import com.example.demo.Region.RegionsDTO;
import com.example.demo.Sessions.SessionsDTO;
import com.example.demo.Skills.SkillsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityRequest {
    private int organizationId;
    private String title;
    private String description;
    private RegionsDTO region;
    private String address;
    private boolean isHighPriority;
    private List<SessionsDTO> sessions;
    private List<Integer> categories;
    private List<Integer> skills;
}
