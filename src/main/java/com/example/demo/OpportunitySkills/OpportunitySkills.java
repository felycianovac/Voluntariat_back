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
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunities opportunity;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skills skill;
}

