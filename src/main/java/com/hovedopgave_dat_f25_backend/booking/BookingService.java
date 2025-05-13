package com.hovedopgave_dat_f25_backend.booking;

import org.springframework.stereotype.Service;

@Service
public class BookingService {

    BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
}
