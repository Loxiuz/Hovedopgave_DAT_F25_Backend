package com.hovedopgave_dat_f25_backend.flight;

import com.hovedopgave_dat_f25_backend.airport.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findFlightByAirportDestination(Airport airportDestination);
    List<Flight> findFlightsByFlightNumber(String flightNumber);
    List<Flight> findFlightsByDepartureTime(String departureTime);
    List<Flight> findFlightsByArrivalTime(String arrivalTime);
}
