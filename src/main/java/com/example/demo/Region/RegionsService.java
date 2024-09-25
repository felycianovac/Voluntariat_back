package com.example.demo.Region;

import com.example.demo.Category.CategoriesDTO;
import com.example.demo.Category.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionsService {
    @Autowired
    private RegionsRepository regionsRepository;

    public List<RegionsDTO> getRegions(){

        return regionsRepository.findAll().stream()
                .map(RegionsDTO::fromEntity)
                .toList();
    }
}
