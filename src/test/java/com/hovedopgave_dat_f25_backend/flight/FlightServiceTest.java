package com.hovedopgave_dat_f25_backend.flight;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    FlightRepository flightRepository;

    @InjectMocks
    FlightService flightService;

    @Test
    void getAllFlights() {
        Flight flight1 = new Flight();
        flight1.setId(1);
        Flight flight2 = new Flight();
        flight2.setId(2);

        when(flightRepository.findAll()).thenReturn(List.of(flight1, flight2));

        List<FlightDTO> flights = flightService.getAllFlights();
        assertEquals(2, flights.size());
        assertEquals(1, flights.get(0).id());
        assertEquals(2, flights.get(1).id());
    }

    @Test
    void getFilteredFlights() {
        Flight flight1 = new Flight();
        flight1.setId(1);
        flight1.setFlightNumber("FL123");

        Flight flight2 = new Flight();
        flight2.setId(2);
        flight2.setFlightNumber("FL456");

        when(flightRepository.findAll()).thenReturn(List.of(flight1, flight2));

        JsonNode filter = mock(JsonNode.class);
        when(filter.get("flight")).thenReturn(mock(JsonNode.class));
        when(filter.get("flight").get("field")).thenReturn(mock(JsonNode.class));
        when(filter.get("flight").get("value")).thenReturn(mock(JsonNode.class));

        when(filter.get("flight").get("field").asText()).thenReturn("flightNumber");
        when(filter.get("flight").get("value").asText()).thenReturn("FL123");

        List<FlightDTO> filteredFlights = flightService.getFilteredFlights(List.of(filter));

        assertEquals(1, filteredFlights.size());
        assertEquals("FL123", filteredFlights.get(0).flightNumber());

    }
}