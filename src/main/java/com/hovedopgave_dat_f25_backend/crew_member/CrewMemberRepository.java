package com.hovedopgave_dat_f25_backend.crew_member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Integer> {
   List<CrewMember> findAllById(Integer id);
    List<CrewMember> findAllByName(String name);
    List<CrewMember> findAllByEmail(String email);
}
