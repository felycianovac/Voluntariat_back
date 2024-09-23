package com.example.demo.Opportunities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunitiesRepository extends JpaRepository<Opportunities, Integer> {
}
