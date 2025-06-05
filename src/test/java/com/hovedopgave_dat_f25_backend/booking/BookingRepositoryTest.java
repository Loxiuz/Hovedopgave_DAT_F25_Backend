package com.hovedopgave_dat_f25_backend.booking;

import com.hovedopgave_dat_f25_backend.flight.Flight;
import com.hovedopgave_dat_f25_backend.flight.FlightRepository;
import com.hovedopgave_dat_f25_backend.passenger.Passenger;
import com.hovedopgave_dat_f25_backend.passenger.PassengerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class BookingRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Test
    void testFlightBookingRelation() {
        Passenger p = new Passenger();
        p.setName("John");
        passengerRepository.save(p);

        Flight f = new Flight();
        f.setFlightNumber("SK123");
        flightRepository.save(f);

        Booking b = new Booking();
        b.setPassenger(p);
        b.setFlight(f);
        b.setBookingNumber("123");
        bookingRepository.save(b);

        List<Booking> bookings = bookingRepository.findAllByFlight(f);
        Assertions.assertEquals(1, bookings.size());
    }

}
