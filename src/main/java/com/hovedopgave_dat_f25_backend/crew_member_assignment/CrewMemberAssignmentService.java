package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CrewMemberAssignmentService {

    CrewMemberAssignmentRepository crewMemberAssignmentRepository;

    public CrewMemberAssignmentService(CrewMemberAssignmentRepository crewMemberAssignmentRepository) {
        this.crewMemberAssignmentRepository = crewMemberAssignmentRepository;
    }

    public List<CrewMemberAssignmentDTO> getAllCrewMemberAssignments() {
        return crewMemberAssignmentRepository.findAll().stream().map(
                this::toDto
        ).collect(Collectors.toList());
    }

    public List<CrewMemberAssignmentDTO> getFilteredCrewMemberAssignments(List<JsonNode> filters) {
        System.out.println("Filters: " + filters);
        List<CrewMemberAssignment> crewMemberAssignments = new ArrayList<>();

        for(JsonNode filter : filters) {
            JsonNode field = filter.get("crew_member_assignment").get("field");
            JsonNode value = filter.get("crew_member_assignment").get("value");

            if(field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();
                System.out.println("Field: " + fieldStr + ", Value: " + valueStr);

                crewMemberAssignments = switch (fieldStr) {
                    case "crewMemberId" -> crewMemberAssignmentRepository.findAllByCrewMemberId(Integer.parseInt(valueStr));
                    case "flightNumber" -> crewMemberAssignmentRepository.findAllByFlightNumber(valueStr);
                    case "role" -> crewMemberAssignmentRepository.findAllByRole(valueStr);
                    default -> crewMemberAssignments;
                };
            }
        }
        if (crewMemberAssignments.isEmpty() && filters.isEmpty()) {
            crewMemberAssignments = crewMemberAssignmentRepository.findAll();
        }

        return crewMemberAssignments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CrewMemberAssignmentDTO toDto(CrewMemberAssignment crewMemberAssignment) {
        return new CrewMemberAssignmentDTO(
                String.valueOf(crewMemberAssignment.getCrewMember().getId()),
                crewMemberAssignment.getFlight().getFlightNumber(),
                crewMemberAssignment.getRole()
        );
    }
}
