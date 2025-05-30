package com.hovedopgave_dat_f25_backend.crew_member;

import com.fasterxml.jackson.databind.JsonNode;
import com.hovedopgave_dat_f25_backend.crew_member_assignment.CrewMemberAssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrewMemberService {

    CrewMemberRepository crewMemberRepository;

    public CrewMemberService(CrewMemberRepository crewMemberRepository) {
        this.crewMemberRepository = crewMemberRepository;
    }

    public List<CrewMemberDTO> getAllCrewMembers() {
        return crewMemberRepository.findAll().stream().map(
                crewMember -> new CrewMemberDTO(
                        String.valueOf(crewMember.getId()),
                        crewMember.getName(),
                        crewMember.getEmail()
                )
        ).toList();
    }

    public List<CrewMemberDTO> getFilteredCrewMembers(List<JsonNode> crewMembers) {
        System.out.println("Filters: " + crewMembers);
        List<CrewMemberDTO> crewMemberList = getAllCrewMembers();

        for(JsonNode filter : crewMembers) {
            JsonNode field = filter.get("crew_member").get("field");
            JsonNode value = filter.get("crew_member").get("value");

            if(field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();
                System.out.println("Field: " + fieldStr + ", Value: " + valueStr);
                if(fieldStr.equals("id")) {
                    crewMemberList = crewMemberList.stream()
                            .filter(crewMember -> crewMember.id().equalsIgnoreCase(valueStr))
                            .toList();
                }
                if (fieldStr.equals("name")) {
                    crewMemberList = crewMemberList.stream()
                            .filter(crewMember -> crewMember.name().equalsIgnoreCase(valueStr))
                            .toList();
                }
                if (fieldStr.equals("email")) {
                    crewMemberList = crewMemberList.stream()
                            .filter(crewMember -> crewMember.email().equalsIgnoreCase(valueStr))
                            .toList();
                }
            }
        }

        return crewMemberList;
    }
}
