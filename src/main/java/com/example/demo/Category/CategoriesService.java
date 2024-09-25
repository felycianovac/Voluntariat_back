package com.example.demo.Category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public List<CategoriesDTO> getCategories(){

        return categoriesRepository.findAll().stream()
                .map(CategoriesDTO::fromEntity)
                .toList();
    }
}
