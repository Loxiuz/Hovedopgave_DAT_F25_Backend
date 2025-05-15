package com.hovedopgave_dat_f25_backend.flight;

import com.hovedopgave_dat_f25_backend.airport.Airport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Airport airportOrigin;

    @ManyToOne
    private Airport airportDestination;

    private String flightNumber;

    private String departureTime;

    private String arrivalTime;

}
