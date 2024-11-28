package com.example.demo.Applications;

import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApplicationRequest {
    private String text;
    private List<String> files;
    private List<Integer> sessions;

}
