package com.example.demo.Sessions;


import com.example.demo.ApplicationSessions.ApplicationSessions;
import com.example.demo.Opportunities.Opportunities;
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
@Table(name = "Sessions")
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private int sessionId;
    @Column(name = "session_date")
    private Date sessionDate;
    @Column(name = "session_start_time")
    private Date sessionStartTime;
    @Column(name = "session_end_time")
    private Date sessionEndTime;
    @Column(name = "spots_left")
    private int spotsLeft;

    @ManyToOne
    @JoinColumn(name = "opportunity_id")
    private Opportunities opportunity;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationSessions> applicationSessions;
}
