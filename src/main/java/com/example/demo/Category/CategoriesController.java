package com.example.demo.Category;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
@AllArgsConstructor
public class CategoriesController {
    private final CategoriesService categoriesService;
    @GetMapping()
    public List<CategoriesDTO> getCategories(){
        return categoriesService.getCategories();
    }

}
