package com.example.demo.Category;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDTO {
    private int id;
    private String name;

    public static CategoriesDTO fromEntity(Categories category) {
        return CategoriesDTO.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
