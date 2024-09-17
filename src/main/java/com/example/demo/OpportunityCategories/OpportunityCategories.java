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
@Entity
@Table(name = "OpportunityCategories")
public class OpportunityCategories {

    @Id //OpportunityCategoryId

    @ManyToOne
    @MapsId("opportunityId")
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunities opportunity;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;
}
