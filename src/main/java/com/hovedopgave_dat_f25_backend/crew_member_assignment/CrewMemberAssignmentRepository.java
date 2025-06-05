package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CrewMemberAssignmentRepository extends JpaRepository<CrewMemberAssignment, Integer> {

    @Query("SELECT c FROM CrewMemberAssignment c WHERE c.crewMember.id = ?1")
    List<CrewMemberAssignment> findAllByCrewMemberId(int crewMemberId);
    @Query("SELECT c FROM CrewMemberAssignment c WHERE c.flight.flightNumber = ?1")
    List<CrewMemberAssignment> findAllByFlightNumber(String flightNumber);
    List<CrewMemberAssignment> findAllByRole(String role);
}
