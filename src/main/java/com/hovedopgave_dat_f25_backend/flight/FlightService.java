package com.hovedopgave_dat_f25_backend.flight;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlightService {

    FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightDTO> getAllFlights() {
    return flightRepository.findAll().stream()
            .map(this::toDto)
            .toList();
    }

    public List<FlightDTO> getFilteredFlights(List<JsonNode> filters) {
        String flightNumber = null;
        String departureTime = null;
        String arrivalTime = null;
        String airportOrigin = null;
        String airportDestination = null;

        for (JsonNode filter : filters) {
            JsonNode field = filter.get("flight").get("field");
            JsonNode value = filter.get("flight").get("value");

            if (field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();

                switch (fieldStr) {
                    case "flightNumber" -> flightNumber = valueStr;
                    case "departureTime" -> departureTime = valueStr;
                    case "arrivalTime" -> arrivalTime = valueStr;
                    case "airportOrigin" -> airportOrigin = valueStr;
                    case "airportDestination" -> airportDestination = valueStr;
                }
            }
        }
        List<Flight> flights;

        if (filters.isEmpty()) {
            flights = flightRepository.findAll();
        } else {
            flights = flightRepository.findAllByFields(
                    flightNumber, departureTime, arrivalTime, airportOrigin, airportDestination
            );
        }

        return flights.stream()
                .map(this::toDto)
                .toList();
    }

    private FlightDTO toDto(Flight flight) {
        return new FlightDTO(
                flight.getId(),
                flight.getAirportOrigin(),
                flight.getAirportDestination(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime()
        );
    }
}
