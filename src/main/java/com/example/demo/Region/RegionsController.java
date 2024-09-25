package com.example.demo.Region;

import com.example.demo.Category.CategoriesDTO;
import com.example.demo.Category.CategoriesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@CrossOrigin
@AllArgsConstructor
public class RegionsController {
    private final RegionsService regionsService;
    @GetMapping()
    public List<RegionsDTO> getRegions(){
        return regionsService.getRegions();
    }

}
