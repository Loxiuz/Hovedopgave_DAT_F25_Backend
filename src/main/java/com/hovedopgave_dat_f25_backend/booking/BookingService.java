package com.hovedopgave_dat_f25_backend.booking;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;
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
        System.out.println("Filters: " + filters);
        List<BookingDTO> bookings = getBookings();

        for(JsonNode filter : filters) {
            JsonNode field = filter.get("booking").get("field");
            JsonNode value = filter.get("booking").get("value");

            if(field != null && value != null) {
                String fieldStr = field.asText();
                String valueStr = value.asText();
                System.out.println("Field: " + fieldStr + ", Value: " + valueStr);

                if (fieldStr.equals("flightNumber")) {
                    bookings = bookings.stream().filter(
                                    booking -> booking.flightNumber().equalsIgnoreCase(valueStr))
                            .collect(Collectors.toList());
                }
                if (fieldStr.equals("status")) {
                    bookings = bookings.stream()
                            .filter(booking -> booking.status().equalsIgnoreCase(valueStr))
                            .collect(Collectors.toList());
                }
                if(fieldStr.equals("passengerId")) {
                    bookings = bookings.stream()
                            .filter(booking -> booking.passengerId().equalsIgnoreCase(valueStr))
                            .collect(Collectors.toList());
                }
            }
        }

        return bookings;
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
