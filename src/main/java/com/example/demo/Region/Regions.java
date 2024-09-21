package com.example.demo.Region;


import com.example.demo.Organization.Organization;
import com.example.demo.User.Users;
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
@Table(name = "Regions")
public class Regions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private int regionId;
    @Column(name = "region_name")
    private String regionName;

    @OneToMany(mappedBy = "region")
    private List<Organization> organizations;

    @OneToMany(mappedBy = "region")
    private List<Users> users;
}
