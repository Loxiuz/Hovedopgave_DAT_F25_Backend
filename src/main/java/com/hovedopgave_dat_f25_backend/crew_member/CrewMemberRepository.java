package com.hovedopgave_dat_f25_backend.crew_member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Integer> {

    @Query("SELECT c FROM CrewMember c WHERE " +
           "(:id IS NULL OR c.id = :id) AND " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:email IS NULL OR c.email LIKE %:email%)")
    List<CrewMember> findAllByFields(
            @Param("id") String id,
            @Param("name") String name,
            @Param("email") String email
    );
}
