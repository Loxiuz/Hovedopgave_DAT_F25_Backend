package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import com.fasterxml.jackson.databind.JsonNode;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberDTO;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CrewMemberAssignmentService {

    CrewMemberAssignmentRepository crewMemberAssignmentRepository;
    CrewMemberService crewMemberService;

    public CrewMemberAssignmentService(CrewMemberAssignmentRepository crewMemberAssignmentRepository, CrewMemberService crewMemberService) {
        this.crewMemberAssignmentRepository = crewMemberAssignmentRepository;
        this.crewMemberService = crewMemberService;
    }

    public List<CrewMemberAssignmentDTO> getAllCrewMemberAssignments() {
        return crewMemberAssignmentRepository.findAll().stream().map(
                this::toDto
        ).collect(Collectors.toList());
    }

    public List<CrewMemberAssignmentDTO> getFilteredCrewMemberAssignments(List<JsonNode> filters) {
        System.out.println("Filters: " + filters);
        List<CrewMemberAssignmentDTO> crewMemberAssignments = getAllCrewMemberAssignments();

        for(JsonNode filter : filters) {
            JsonNode field = filter.get("crew_member_assignment").get("field");
            JsonNode value = filter.get("crew_member_assignment").get("value");

            if(field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();
                System.out.println("Field: " + fieldStr + ", Value: " + valueStr);
                    if(fieldStr.equals("crewMemberId")) {
                        crewMemberAssignments = crewMemberAssignments.stream()
                                .filter(assignment -> assignment.crewMemberId().equalsIgnoreCase(valueStr))
                                .toList();
                    }
                    if (fieldStr.equals("role")) {
                        crewMemberAssignments = crewMemberAssignments.stream()
                                .filter(assignment -> assignment.role().equalsIgnoreCase(valueStr))
                                .toList();
                    }
                    if (fieldStr.equals("flightNumber")) {
                        crewMemberAssignments = crewMemberAssignments.stream()
                                .filter(assignment -> assignment.flightNumber().equalsIgnoreCase(valueStr))
                                .toList();
                    }
            }
        }

        return crewMemberAssignments;
    }

    private CrewMemberAssignmentDTO toDto(CrewMemberAssignment crewMemberAssignment) {
        return new CrewMemberAssignmentDTO(
                String.valueOf(crewMemberAssignment.getCrewMember().getId()),
                crewMemberAssignment.getFlight().getFlightNumber(),
                crewMemberAssignment.getRole()
        );
    }
}
