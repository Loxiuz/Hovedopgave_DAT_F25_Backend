package com.hovedopgave_dat_f25_backend.flight;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightDTO> getAllFlights() {
    return flightRepository.findAll()
            .stream()
            .map(flight -> new FlightDTO(
                    flight.getId(),
                    flight.getAirportOrigin(),
                    flight.getAirportDestination(),
                    flight.getFlightNumber(),
                    flight.getDepartureTime(),
                    flight.getArrivalTime()))
            .toList();
    }

    public List<FlightDTO> getFilteredFlights(List<JsonNode> filters) {
        System.out.println("Filters: " + filters);
        List<FlightDTO> flights = getAllFlights();

        for (JsonNode filter : filters) {
            JsonNode field = filter.get("flight").get("field");
            JsonNode value = filter.get("flight").get("value");

            if (field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();
                System.out.println("Field: " + fieldStr + ", Value: " + valueStr);

                if (fieldStr.equals("flightNumber")) {
                    flights = flights.stream()
                            .filter(flight -> flight.flightNumber().equalsIgnoreCase(valueStr))
                            .toList();
                }
                if (fieldStr.equals("departureTime")) {
                    flights = flights.stream()
                            .filter(flight -> flight.departureTime().equalsIgnoreCase(valueStr))
                            .toList();
                }
                if (fieldStr.equals("arrivalTime")) {
                    flights = flights.stream()
                            .filter(flight -> flight.arrivalTime().equalsIgnoreCase(valueStr))
                            .toList();
                }
                if (fieldStr.equals("airportOrigin")) {
                    flights = flights.stream()
                            .filter(flight -> String.valueOf(flight.airportOrigin().getId()).equalsIgnoreCase(valueStr))
                            .toList();
                }
                if (fieldStr.equals("airportDestination")) {
                    flights = flights.stream()
                            .filter(flight -> String.valueOf(flight.airportDestination().getId()).equalsIgnoreCase(valueStr))
                            .toList();
                }
            }
        }

        return flights;
    }
}
