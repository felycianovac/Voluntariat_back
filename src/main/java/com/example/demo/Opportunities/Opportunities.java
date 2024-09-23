package com.example.demo.Opportunities;

import com.example.demo.OpportunityCategories.OpportunityCategories;
import com.example.demo.OpportunitySkills.OpportunitySkills;
import com.example.demo.Organization.ApprovalStatus;
import com.example.demo.Organization.Organization;
import com.example.demo.Region.Regions;
import com.example.demo.Sessions.Sessions;
import com.example.demo.User.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Opportunities")
public class Opportunities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private Users createdByUser;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Regions region;


    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Users approvedBy;

    @Column(name = "approval_date")
    private Date approvalDate;

    @Column(name = "is_high_priority")
    private boolean isHighPriority = false;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "opportunity")
    private List<Sessions> sessions;

    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpportunityCategories> opportunityCategories;

    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL)
    private List<OpportunitySkills> opportunitySkills;

}
