package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import com.fasterxml.jackson.databind.JsonNode;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMember;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberDTO;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
import com.hovedopgave_dat_f25_backend.flight.Flight;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrewMemberAssignmentServiceTest {

    @Mock
    CrewMemberAssignmentRepository crewMemberAssignmentRepository;
    @Mock
    CrewMemberService crewMemberService;

    @InjectMocks
    CrewMemberAssignmentService crewMemberAssignmentService;

    @Test
    void getAllCrewMemberAssignments() {
        CrewMember crewMember1 = new CrewMember();
        crewMember1.setId(1);
        CrewMember crewMember2 = new CrewMember();
        crewMember2.setId(2);
        Flight flight1 = new Flight();
        flight1.setFlightNumber("FL123");
        Flight flight2 = new Flight();
        flight2.setFlightNumber("FL456");

        CrewMemberAssignment crewMemberAssignment1 = new CrewMemberAssignment();
        crewMemberAssignment1.setCrewMember(crewMember1);
        crewMemberAssignment1.setFlight(flight1);
        CrewMemberAssignment crewMemberAssignment2 = new CrewMemberAssignment();
        crewMemberAssignment2.setCrewMember(crewMember2);
        crewMemberAssignment2.setFlight(flight2);

        when(crewMemberAssignmentRepository.findAll()).thenReturn(List.of(crewMemberAssignment1, crewMemberAssignment2));

        List<CrewMemberAssignmentDTO> assignments = crewMemberAssignmentService.getAllCrewMemberAssignments();
        assertEquals(2, assignments.size());
        assertEquals("1", assignments.get(0).crewMemberId());
        assertEquals("FL123", assignments.get(0).flightNumber());
        assertEquals("2", assignments.get(1).crewMemberId());
        assertEquals("FL456", assignments.get(1).flightNumber());
    }

    @Test
    void getFilteredCrewMembers() {
        CrewMember crewMember1 = new CrewMember();
        crewMember1.setId(1);
        CrewMember crewMember2 = new CrewMember();
        crewMember2.setId(2);
        Flight flight1 = new Flight();
        flight1.setFlightNumber("FL123");
        Flight flight2 = new Flight();
        flight2.setFlightNumber("FL456");

        CrewMemberAssignment crewMemberAssignment1 = new CrewMemberAssignment();
        crewMemberAssignment1.setCrewMember(crewMember1);
        crewMemberAssignment1.setFlight(flight1);
        CrewMemberAssignment crewMemberAssignment2 = new CrewMemberAssignment();
        crewMemberAssignment2.setCrewMember(crewMember2);
        crewMemberAssignment2.setFlight(flight2);

        when(crewMemberService.getAllCrewMembers()).thenReturn(List.of(
                new CrewMemberDTO("1"),
                new CrewMemberDTO("2")
        ));
        when(crewMemberAssignmentRepository.findAll()).thenReturn(List.of(crewMemberAssignment1, crewMemberAssignment2));

        JsonNode filter = mock(JsonNode.class);
        when(filter.get("crew_member")).thenReturn(mock(JsonNode.class));
        when(filter.get("crew_member").get("field")).thenReturn(mock(JsonNode.class));
        when(filter.get("crew_member").get("value")).thenReturn(mock(JsonNode.class));

        when(filter.get("crew_member").get("field").asText()).thenReturn("flightNumber");
        when(filter.get("crew_member").get("value").asText()).thenReturn("FL123");

        List<CrewMemberDTO> filteredCrewMembers = crewMemberAssignmentService.getFilteredCrewMembers(List.of(filter));

        assertEquals(2, filteredCrewMembers.size());
        assertEquals("1", filteredCrewMembers.get(0).id());
    }
}