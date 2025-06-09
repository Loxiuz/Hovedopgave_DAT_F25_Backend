package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewMemberAssignmentRepository extends JpaRepository<CrewMemberAssignment, Integer> {

    @Query("SELECT c FROM CrewMemberAssignment c WHERE c.flight.flightNumber = ?1")
    List<CrewMemberAssignment> findAllByFlightNumber(String flightNumber);

    @Query("SELECT c FROM CrewMemberAssignment c WHERE " +
           "(:crewMemberId IS NULL OR c.crewMember.id = :crewMemberId) AND " +
           "(:flightNumber IS NULL OR c.flight.flightNumber = :flightNumber) AND " +
           "(:role IS NULL OR c.role = :role)")
    List<CrewMemberAssignment> findAllByFields(
            @Param("crewMemberId") String crewMemberId,
            @Param("flightNumber") String flightNumber,
            @Param("role") String role
    );
}
