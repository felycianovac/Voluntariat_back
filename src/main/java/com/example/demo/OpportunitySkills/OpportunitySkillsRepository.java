package com.example.demo.OpportunitySkills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunitySkillsRepository extends JpaRepository<OpportunitySkills, Integer> {
}
