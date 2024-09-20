package com.example.demo.Region;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionsDTO {
    private int id;
    private String name;

    public static RegionsDTO fromEntity(Regions region) {
        return RegionsDTO.builder()
                .id(region.getRegionId())
                .name(region.getRegionName())
                .build();
    }
}
