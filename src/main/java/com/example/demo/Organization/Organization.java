package com.example.demo.Organization;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false, name = "name")
    private String name;
    @Column(nullable = false, name = "description")
    private String description;
    @Column(nullable = false, name = "address")
    private String address;
    @Column(nullable = false, name = "region")
    private int region;
    @Column(nullable = false, name = "website")
    private String website;
    @Column(nullable = false, name = "phoneNubmer")
    private String phoneNumber;
    @Column(nullable = false, name = "logo")
    private String logo;
    @Column(nullable = false, name = "approvalStatus")
    private String approvalStatus;

    @ElementCollection
    private List<Integer> categories;

}

