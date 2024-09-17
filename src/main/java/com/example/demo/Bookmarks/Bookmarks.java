package com.example.demo.Bookmarks;
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
@Table(name = "Bookmarks")
public class Bookmarks {

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

    @Column(name = "created_at")
    private Date createdAt;

}
