package com.example.demo.ApplicationSessions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationSessionsRepository extends JpaRepository<ApplicationSessions, Integer> {
}
