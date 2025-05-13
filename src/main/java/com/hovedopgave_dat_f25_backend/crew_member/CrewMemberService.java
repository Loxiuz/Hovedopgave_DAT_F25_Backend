package com.hovedopgave_dat_f25_backend.crew_member;

import org.springframework.stereotype.Service;

@Service
public class CrewMemberService {

    CrewMemberRepository crewMemberRepository;

    public CrewMemberService(CrewMemberRepository crewMemberRepository) {
        this.crewMemberRepository = crewMemberRepository;
    }
}
