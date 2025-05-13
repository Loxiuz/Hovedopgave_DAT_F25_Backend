package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import org.springframework.stereotype.Service;

@Service
public class CrewMemberAssignmentService {

    CrewMemberAssignmentRepository crewMemberAssignmentRepository;

    public CrewMemberAssignmentService(CrewMemberAssignmentRepository crewMemberAssignmentRepository) {
        this.crewMemberAssignmentRepository = crewMemberAssignmentRepository;
    }
}
