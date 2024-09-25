package com.example.demo.Region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionsRepository extends JpaRepository<Regions, Integer> {
}
