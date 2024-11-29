package com.example.demo.Applications;

import com.example.demo.Opportunities.OpportunityDTO2;
import com.example.demo.Sessions.SessionsDTO;
import com.example.demo.User.UserDTO3;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ApplicationResponseDTO {
    private int id;
    private UserDTO3 applicant;
    private OpportunityDTO2 opportunity;
    private String text;
    private List<String> files;
    private List<SessionsDTO> sessions;
    private Date createdAt;
    private Date updatedAt;
    private String approvalStatus;
    private Date approvalDate;
}
