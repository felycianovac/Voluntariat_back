package com.example.demo.Reviews;

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
@Table(name = "Reviews")
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private Users reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewee_id")
    private Users reviewee;

    @ManyToOne
    @JoinColumn(name = "opportunity_id")
    private Opportunities opportunity;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at")
    private Date createdAt;

}
