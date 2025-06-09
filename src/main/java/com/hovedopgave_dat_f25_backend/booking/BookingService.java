package com.hovedopgave_dat_f25_backend.booking;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDTO> getBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getFilteredBookings(List<JsonNode> filters) {
        String passengerId = null;
        String flightNumber = null;
        String bookingNumber = null;
        String seatNumber = null;
        String status = null;

        for (JsonNode filter : filters) {
            JsonNode fieldNode = filter.get("booking").get("field");
            JsonNode valueNode = filter.get("booking").get("value");

            if (fieldNode != null && valueNode != null) {
                String field = fieldNode.asText();
                String value = valueNode.asText();

                switch (field) {
                    case "passengerId" -> passengerId = value;
                    case "flightNumber" -> flightNumber = value;
                    case "bookingNumber" -> bookingNumber = value;
                    case "seatNumber" -> seatNumber = value;
                    case "status" -> status = value;
                }
            }
        }

        List<Booking> bookings;

        if (filters.isEmpty()) {
            bookings = bookingRepository.findAll();
        } else {
            bookings = bookingRepository.findAllByFields(
                    passengerId, flightNumber, bookingNumber, seatNumber, status
            );
        }

        return bookings.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private BookingDTO toDto(Booking booking) {
        return new BookingDTO(
               String.valueOf(booking.getPassenger().getId()),
                booking.getFlight().getFlightNumber(),
                booking.getBookingNumber(),
                booking.getSeatNumber(),
                booking.getStatus()
        );
    }
}
