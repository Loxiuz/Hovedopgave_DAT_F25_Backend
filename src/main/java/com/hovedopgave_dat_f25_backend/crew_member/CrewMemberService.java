package com.hovedopgave_dat_f25_backend.crew_member;

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
                        crewMember.getId(),
                        crewMember.getName(),
                        crewMember.getEmail(),
                        crewMember.getPhoneNumber()
                )
        ).toList();
    }
}
