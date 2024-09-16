package com.example.demo.Organization;

import com.example.demo.Category.Categories;
import com.example.demo.User.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private int organizationId;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "address")
    private String address;
    @Column(name = "region_id")
    private int regionId; //TODO: change to region object
    @Column(name = "website")
    private String website;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "logo")
    private String logo;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    private Users createdBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;
    @ManyToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private Users approvedBy;
    @Column(name = "approval_date")
    private Date approvalDate;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrganizationCategories> organizationCategories;


}

