package com.example.demo.OpportunityCategories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunitiesCategoryRepository  extends JpaRepository<OpportunityCategories, Integer> {

}
