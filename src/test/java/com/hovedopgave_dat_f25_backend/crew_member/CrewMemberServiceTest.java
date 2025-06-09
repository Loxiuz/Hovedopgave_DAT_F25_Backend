package com.hovedopgave_dat_f25_backend.crew_member;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrewMemberServiceTest {

    @Mock
    CrewMemberRepository crewMemberRepository;

    @InjectMocks
    CrewMemberService crewMemberService;

    @Test
    void getAllCrewMembers() {
        CrewMember crewMember1 = new CrewMember();
        crewMember1.setId(1);
        CrewMember crewMember2 = new CrewMember();
        crewMember2.setId(2);

        when(crewMemberRepository.findAll()).thenReturn(List.of(crewMember1, crewMember2));

        List<CrewMemberDTO> result = crewMemberService.getAllCrewMembers();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("2", result.get(1).id());
    }

    @Test
    void testGetFilteredCrewMembers() {
        CrewMember crewMember1 = new CrewMember();
        crewMember1.setId(1);

        when(crewMemberRepository.findAllByFields(any(), any(), any()))
                .thenReturn(List.of(crewMember1));

        JsonNode filter = mock(JsonNode.class);
        when(filter.get("crew_member")).thenReturn(mock(JsonNode.class));
        when(filter.get("crew_member").get("field")).thenReturn(mock(JsonNode.class));
        when(filter.get("crew_member").get("value")).thenReturn(mock(JsonNode.class));

        when(filter.get("crew_member").get("field").asText()).thenReturn("id");
        when(filter.get("crew_member").get("value").asText()).thenReturn("1");

        List<CrewMemberDTO> result = crewMemberService.getFilteredCrewMembers(List.of(filter));
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());

    }
}