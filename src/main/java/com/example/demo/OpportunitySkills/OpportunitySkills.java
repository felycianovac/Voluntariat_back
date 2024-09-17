package com.example.demo.OpportunitySkills;

import com.example.demo.Opportunities.Opportunities;
import com.example.demo.Skills.Skills;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OpportunitySkills")
public class OpportunitySkills {

    @Id //OpportunitySkillId ?

    @ManyToOne
    @MapsId("opportunityId")
    @JoinColumn(name = "opportunity_id")
    private Opportunities opportunity;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skills skill;
}
