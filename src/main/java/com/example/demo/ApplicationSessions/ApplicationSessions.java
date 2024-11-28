package com.example.demo.ApplicationSessions;

import com.example.demo.Applications.Applications;
import com.example.demo.Sessions.Sessions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_sessions")
public class ApplicationSessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Applications application;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Sessions session;
}
