package com.hovedopgave_dat_f25_backend.crew_member;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrewMemberService {

    CrewMemberRepository crewMemberRepository;

    public CrewMemberService(CrewMemberRepository crewMemberRepository) {
        this.crewMemberRepository = crewMemberRepository;
    }

    public List<CrewMemberDTO> getAllCrewMembers() {
        return crewMemberRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<CrewMemberDTO> getFilteredCrewMembers(List<JsonNode> filters) {
        String id = null;
        String name = null;
        String email = null;

        for (JsonNode filter : filters) {
            JsonNode field = filter.get("crew_member").get("field");
            JsonNode value = filter.get("crew_member").get("value");

            if (field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();

                switch (fieldStr) {
                    case "id" -> id = valueStr;
                    case "name" -> name = valueStr;
                    case "email" -> email = valueStr;
                }

            }

        }
        List<CrewMember> crewMembers = new ArrayList<>();

        if (filters.isEmpty()) {
            crewMembers = crewMemberRepository.findAll();
        } else {
            crewMembers = crewMemberRepository.findAllByFields(id, name, email);
        }
        return crewMembers.stream()
                .map(this::toDto)
                .toList();
    }

    private CrewMemberDTO toDto(CrewMember crewMember) {
        return new CrewMemberDTO(
                String.valueOf(crewMember.getId()),
                crewMember.getName(),
                crewMember.getEmail()
        );
    }
}
