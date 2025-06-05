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
        System.out.println("Filters: " + filters);
        List<Flight> flights = new ArrayList<>();

        for (JsonNode filter : filters) {
            JsonNode field = filter.get("flight").get("field");
            JsonNode value = filter.get("flight").get("value");

            if (field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();

                flights = switch (fieldStr) {
                    case "flightNumber" -> flightRepository.findFlightsByFlightNumber(valueStr);
                    case "departureTime" -> flightRepository.findFlightsByDepartureTime(valueStr);
                    case "arrivalTime" -> flightRepository.findFlightsByArrivalTime(valueStr);
                    default -> flights;
                };
            }
        }
        if (flights.isEmpty() && filters.isEmpty()) {
            flights = flightRepository.findAll();
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
