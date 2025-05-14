package com.hovedopgave_dat_f25_backend.flight;

import com.hovedopgave_dat_f25_backend.airport.Airport;

public record FlightDTO(int id, Airport airportOrigin, Airport airportDestination, String flightNumber, String departureTime, String arrivalTime) {
}
