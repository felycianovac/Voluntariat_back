package com.example.demo.Applications;

import com.example.demo.Opportunities.Opportunities;
import com.example.demo.User.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Integer> {

    List<Applications> findByOpportunity(Opportunities opportunity);

    List<Applications> findByVolunteer(Users volunteer);
}
