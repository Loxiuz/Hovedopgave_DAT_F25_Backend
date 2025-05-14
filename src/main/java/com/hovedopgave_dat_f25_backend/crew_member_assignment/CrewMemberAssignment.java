package com.hovedopgave_dat_f25_backend.crew_member_assignment;

import com.hovedopgave_dat_f25_backend.crew_member.CrewMember;
import com.hovedopgave_dat_f25_backend.flight.Flight;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CrewMemberAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private CrewMember crewMember;

    @ManyToOne
    private Flight flight;

    private String role;

}
