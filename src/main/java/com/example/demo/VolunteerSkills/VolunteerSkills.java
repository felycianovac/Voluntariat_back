package com.example.demo.VolunteerSkills;

import  com.example.demo.Skills.Skills;
import com.example.demo.User.Users;
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
@Table(name = "volunteer_skills")
public class VolunteerSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Users volunteer;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skills skill;
}
