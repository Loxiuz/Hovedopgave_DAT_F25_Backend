package com.hovedopgave_dat_f25_backend.flight;

import com.hovedopgave_dat_f25_backend.airport.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findFlightByAirportDestination(Airport airportDestination);

    List<Flight> findFlightsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE " +
           "(:flightNumber IS NULL OR f.flightNumber = :flightNumber) AND " +
           "(:departureTime IS NULL OR f.departureTime = :departureTime) AND " +
           "(:arrivalTime IS NULL OR f.arrivalTime = :arrivalTime) AND " +
           "(:airportOrigin IS NULL OR f.airportOrigin.id = :airportOrigin) AND " +
           "(:airportDestination IS NULL OR f.airportDestination.id = :airportDestination)")
    List<Flight> findAllByFields(
            @Param("flightNumber") String flightNumber,
            @Param("departureTime") String departureTime,
            @Param("arrivalTime") String arrivalTime,
            @Param("airportOrigin") String airportOrigin,
            @Param("airportDestination") String airportDestination
    );
}
