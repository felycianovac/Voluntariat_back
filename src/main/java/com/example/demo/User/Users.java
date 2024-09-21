package com.example.demo.User;

import com.example.demo.Organization.Organization;
import com.example.demo.Region.Regions;
import com.example.demo.VolunteerSkills.VolunteerSkills;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class Users implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Roles role;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "bio")
    private String bio;
    @Column(name = "profile_picture")
    private String profilePicture;
    @Column(name = "is_profile_completed")
    private boolean isProfileCompleted;
    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "region_id")
    private Regions region;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "is_first_login")
    private boolean isFirstLogin;

    @OneToMany(mappedBy = "approvedBy", fetch = FetchType.EAGER)
    private List<Organization> approvedOrganizations;

    @OneToMany(mappedBy = "createdBy")
    private List<Organization> createdOrganizations;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL)
    private List<VolunteerSkills> volunteerSkills;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name().toUpperCase()));
    }


    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return isProfileCompleted;
    }



}
