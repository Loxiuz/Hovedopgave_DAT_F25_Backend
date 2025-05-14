package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import com.hovedopgave_dat_f25_backend.crew_member.CrewMember;
import com.hovedopgave_dat_f25_backend.flight.Flight;

public record CrewMemberAssignmentDTO(int id, CrewMember crewMember, Flight flight, String role) {
}
