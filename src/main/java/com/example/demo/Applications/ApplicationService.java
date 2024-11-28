package com.example.demo.Applications;

import com.example.demo.ApplicationFiles.ApplicationFiles;
import com.example.demo.ApplicationFiles.ApplicationFilesRepository;
import com.example.demo.ApplicationSessions.ApplicationSessions;
import com.example.demo.ApplicationSessions.ApplicationSessionsRepository;
import com.example.demo.Opportunities.Opportunities;
import com.example.demo.Opportunities.OpportunitiesRepository;
import com.example.demo.Opportunities.OpportunityDTO2;
import com.example.demo.Organization.ApprovalStatus;
import com.example.demo.Sessions.Sessions;
import com.example.demo.Sessions.SessionsDTO;
import com.example.demo.Sessions.SessionsRepository;
import com.example.demo.User.UserDTO3;
import com.example.demo.User.Users;
import com.example.demo.User.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final UsersRepository usersRepository;
    private final OpportunitiesRepository opportunitiesRepository;
    private final ApplicationsRepository applicationsRepository;
    private final SessionsRepository sessionsRepository;
    private final ApplicationFilesRepository applicationFilesRepository;
    private final ApplicationSessionsRepository applicationSessionsRepository;

    @Transactional
    public ApplicationResponse submitApplication(int opportunityId, ApplicationRequest request, String volunteerEmail) {
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            return new ApplicationResponse("error", "Motivation text is required.");
        }

        Opportunities opportunity = opportunitiesRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        if (!ApprovalStatus.approved.equals(opportunity.getApprovalStatus())) {
            return new ApplicationResponse("error", "Cannot apply to an unapproved opportunity.");
        }

        Users volunteer = usersRepository.findByEmail(volunteerEmail)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        List<Sessions> linkedSessions = validateAndLinkSessions(request.getSessions(), opportunity);

        Applications application = Applications.builder()
                .volunteer(volunteer)
                .opportunity(opportunity)
                .motivationText(request.getText())
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(ApplicationStatus.PENDING)
                .build();

        applicationsRepository.save(application);
        if (request.getSessions() != null && !request.getSessions().isEmpty()) {
            linkSessionsToApplication(request.getSessions(), application, opportunity);
        }

        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            saveApplicationFiles(request.getFiles(), application);
        }

        sessionsRepository.saveAll(linkedSessions);

        return new ApplicationResponse("success", "Your application was successfully submitted!");
    }


    private List<Sessions> validateAndLinkSessions(List<Integer> sessionIds, Opportunities opportunity) {
        List<Sessions> linkedSessions = new ArrayList<>();

        if (sessionIds != null && !sessionIds.isEmpty()) {
            for (Integer sessionId : sessionIds) {
                Sessions session = sessionsRepository.findById(sessionId)
                        .orElseThrow(() -> new RuntimeException("Session with ID " + sessionId + " not found"));

                if (!session.getOpportunity().equals(opportunity)) {
                    throw new RuntimeException("Session with ID " + sessionId + " does not belong to the specified opportunity");
                }

                if (session.getSpotsLeft() <= 0) {
                    throw new RuntimeException("Session with ID " + sessionId + " has no spots left");
                }

                session.setSpotsLeft(session.getSpotsLeft() - 1);
                linkedSessions.add(session);
            }
        }

        return linkedSessions;
    }

    private void linkSessionsToApplication(List<Integer> sessionIds, Applications application, Opportunities opportunity) {
        List<ApplicationSessions> applicationSessions = new ArrayList<>();

        for (Integer sessionId : sessionIds) {
            Sessions session = sessionsRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session with ID " + sessionId + " not found"));

            if (!session.getOpportunity().equals(opportunity)) {
                throw new RuntimeException("Session with ID " + sessionId + " does not belong to the specified opportunity");
            }

            if (session.getSpotsLeft() <= 0) {
                throw new RuntimeException("Session with ID " + sessionId + " has no spots left");
            }

//            session.setSpotsLeft(session.getSpotsLeft() - 1);

            ApplicationSessions applicationSession = ApplicationSessions.builder()
                    .application(application)
                    .session(session)
                    .build();

            applicationSessions.add(applicationSession);
        }

        sessionsRepository.saveAll(applicationSessions.stream()
                .map(ApplicationSessions::getSession)
                .toList());

        applicationSessionsRepository.saveAll(applicationSessions);
    }




    private void saveApplicationFiles(List<String> fileLinks, Applications application) {
        List<ApplicationFiles> applicationFiles = fileLinks.stream()
                .map(fileLink -> ApplicationFiles.builder()
                        .application(application)
                        .fileLink(fileLink)
                        .build())
                .toList();

//        application.getApplicationFiles().addAll(applicationFiles);


        applicationFilesRepository.saveAll(applicationFiles);
    }

    public List<ApplicationResponseDTO> getApplicationsByOpportunityId(int opportunityId, String currentUserEmail) {
        Opportunities opportunity = opportunitiesRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        Users currentUser = usersRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!opportunity.getCreatedByUser().equals(currentUser) &&
                !opportunity.getOrganization().getCreatedBy().equals(currentUser)) {
            throw new RuntimeException("Access denied: You do not have permission to view these applications.");
        }

        List<Applications> applications = applicationsRepository.findByOpportunity(opportunity);

        return applications.stream().map(application -> ApplicationResponseDTO.builder()
                .id(application.getId())
                .user(UserDTO3.builder()
                        .id(application.getVolunteer().getUserId())
                        .firstName(application.getVolunteer().getFirstName())
                        .lastName(application.getVolunteer().getLastName())
                        .build())
                .opportunity(OpportunityDTO2.fromEntity(application.getOpportunity()))
                .text(application.getMotivationText())
                .files(application.getApplicationFiles().stream()
                        .map(ApplicationFiles::getFileLink)
                        .collect(Collectors.toList()))
                .sessions(application.getApplicationSessions().stream()
                        .map(appSession -> SessionsDTO.builder()
                                .id(appSession.getSession().getSessionId())
                                .date(appSession.getSession().getSessionDate())
                                .startTime(appSession.getSession().getSessionStartTime())
                                .endTime(appSession.getSession().getSessionEndTime())
                                .spotsLeft(appSession.getSession().getSpotsLeft())
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .approvalStatus(application.getStatus().name().toLowerCase())
                .approvalDate(application.getApprovalDate())
                .build()
        ).collect(Collectors.toList());
    }

}
