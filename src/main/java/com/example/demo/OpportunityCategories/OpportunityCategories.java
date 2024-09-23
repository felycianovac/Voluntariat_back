package com.example.demo.OpportunityCategories;

import com.example.demo.Category.Categories;
import com.example.demo.Opportunities.Opportunities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "opportunity_categories")
public class OpportunityCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunities opportunity;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;
}
