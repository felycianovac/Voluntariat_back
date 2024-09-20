package com.example.demo.Applications;

import com.example.demo.Opportunities.Opportunities;
import com.example.demo.User.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Applications")
public class Applications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Users volunteer;

    @ManyToOne
    @JoinColumn(name = "opportunity_id")
    private Opportunities opportunity;

//    @ManyToOne
//    @JoinColumn(name = "session_id")
//    private OpportunitySessions session;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private ApplicationStatus status; // Assuming an enum named ApplicationStatus with values: PENDING, APPROVED, DECLINED, COMPLETED

    @Column(name = "motivation_text", columnDefinition = "TEXT")
    private String motivationText;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
