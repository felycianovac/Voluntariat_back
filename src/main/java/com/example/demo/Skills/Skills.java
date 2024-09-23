package com.example.demo.Skills;

import com.example.demo.OpportunitySkills.OpportunitySkills;
import com.example.demo.VolunteerSkills.VolunteerSkills;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skills")
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<VolunteerSkills> volunteerSkills;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<OpportunitySkills> opportunitySkills;
}
