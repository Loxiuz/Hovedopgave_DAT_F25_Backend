package com.hovedopgave_dat_f25_backend.booking;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDTO> getBookings() {
        return bookingRepository.findAll().stream().map(
                booking -> new BookingDTO(
                        booking.getId(),
                        booking.getPassenger(),
                        booking.getFlight(),
                        booking.getBookingNumber(),
                        booking.getSeatNumber(),
                        booking.getStatus()
                )
        ).toList();
    }
}
