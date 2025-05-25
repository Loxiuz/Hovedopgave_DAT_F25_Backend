package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import com.fasterxml.jackson.databind.JsonNode;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberDTO;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public List<CrewMemberDTO> getFilteredCrewMembers(List<JsonNode> filters) {
        System.out.println("Filters: " + filters);
        List<CrewMemberDTO> crewMembers = crewMemberService.getAllCrewMembers();
        List<CrewMemberAssignmentDTO> crewMemberAssignments = getAllCrewMemberAssignments();

        for(JsonNode filter : filters) {
            JsonNode field = filter.get("crewMember").get("field");
            JsonNode value = filter.get("crewMember").get("value");

            if(field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();
                System.out.println("Field: " + fieldStr + ", Value: " + valueStr);

                if (filter.has("crewMember")) {
                    if (fieldStr.equals("flightNumber")) {
                        crewMembers = crewMembers.stream().filter(
                                crewMember -> crewMemberAssignments.stream()
                                        .anyMatch(assignment -> assignment.flightNumber().equalsIgnoreCase(valueStr)
                                                && assignment.crewMemberId().equals(crewMember.id()))
                        ).collect(Collectors.toList());
                    }
                    if (fieldStr.equals("role")) {
                        crewMembers = crewMembers.stream().filter(
                                crewMember -> crewMemberAssignments.stream()
                                        .anyMatch(assignment -> assignment.role().equalsIgnoreCase(valueStr)
                                                && assignment.crewMemberId().equals(crewMember.id()))
                        ).collect(Collectors.toList());
                    }
                }
            }
        }

        return crewMembers;
    }

    public CrewMemberAssignmentDTO toDto(CrewMemberAssignment crewMemberAssignment) {
        return new CrewMemberAssignmentDTO(
                crewMemberAssignment.getId(),
                crewMemberAssignment.getFlight().getFlightNumber(),
                String.valueOf(crewMemberAssignment.getCrewMember().getId()),
                crewMemberAssignment.getRole()
        );
    }
}
