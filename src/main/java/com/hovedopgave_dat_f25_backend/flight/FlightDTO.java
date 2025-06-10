package com.hovedopgave_dat_f25_backend.flight;

public record FlightDTO(int airportOriginId, int airportDestinationId, String flightNumber, String departureTime, String arrivalTime) {
}
