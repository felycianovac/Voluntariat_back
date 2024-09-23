package com.example.demo.Sessions;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionsDTO {
    private Date date;
    private Date startTime;
    private Date endTime;
    private int spotsLeft;

    public static SessionsDTO fromEntity(Sessions session) {
        return SessionsDTO.builder()
                .date(session.getSessionDate())
                .startTime(session.getSessionStartTime())
                .endTime(session.getSessionEndTime())
                .spotsLeft(session.getSpotsLeft())
                .build();
    }
}
