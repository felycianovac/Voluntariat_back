package com.example.demo.VolunteerSkills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerSkillsRepository extends JpaRepository<VolunteerSkills, Integer> {
}

