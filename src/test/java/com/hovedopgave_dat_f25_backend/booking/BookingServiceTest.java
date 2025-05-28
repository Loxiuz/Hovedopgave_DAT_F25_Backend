package com.hovedopgave_dat_f25_backend.booking;

import com.fasterxml.jackson.databind.JsonNode;
import com.hovedopgave_dat_f25_backend.flight.Flight;
import com.hovedopgave_dat_f25_backend.passenger.Passenger;
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
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    BookingService bookingService;

    @Test
    void getBookings() {
        Flight flight1 = new Flight();
        flight1.setId(1);
        flight1.setFlightNumber("FL123");
        Flight flight2 = new Flight();
        flight2.setId(2);
        flight2.setFlightNumber("FL456");

        Passenger passenger1 = new Passenger();
        passenger1.setId(1);
        Passenger passenger2 = new Passenger();
        passenger2.setId(2);

        Booking booking1 = new Booking();
        booking1.setId(1);
        booking1.setFlight(flight1);
        booking1.setPassenger(passenger1);
        booking1.setStatus("COMPLETED");
        Booking booking2 = new Booking();
        booking2.setId(2);
        booking2.setFlight(flight2);
        booking2.setPassenger(passenger2);
        booking1.setStatus("COMPLETED");

        when(bookingRepository.findAll()).thenReturn(List.of(booking1, booking2));

        List<BookingDTO> bookings = bookingService.getBookings();
        assertEquals(2, bookings.size());
        assertEquals("1", bookings.get(0).passengerId());
        assertEquals("FL123", bookings.get(0).flightNumber());
        assertEquals("COMPLETED", bookings.get(0).status());
    }

    @Test
    void getFilteredBookings() {
        Flight flight1 = new Flight();
        flight1.setId(1);
        flight1.setFlightNumber("FL123");

        Passenger passenger1 = new Passenger();
        passenger1.setId(1);

        Booking booking1 = new Booking();
        booking1.setId(1);
        booking1.setFlight(flight1);
        booking1.setPassenger(passenger1);
        booking1.setStatus("CONFIRMED");

        when(bookingRepository.findAll()).thenReturn(List.of(booking1));

        JsonNode filter = mock(JsonNode.class);
        when(filter.get("booking")).thenReturn(mock(JsonNode.class));
        when(filter.get("booking").get("field")).thenReturn(mock(JsonNode.class));
        when(filter.get("booking").get("value")).thenReturn(mock(JsonNode.class));

        when(filter.get("booking").get("field").asText()).thenReturn("flightNumber");
        when(filter.get("booking").get("value").asText()).thenReturn("FL123");

        List<BookingDTO> filteredBookings = bookingService.getFilteredBookings(List.of(filter));

        assertEquals(1, filteredBookings.size());
        assertEquals("FL123", filteredBookings.get(0).flightNumber());
    }
}