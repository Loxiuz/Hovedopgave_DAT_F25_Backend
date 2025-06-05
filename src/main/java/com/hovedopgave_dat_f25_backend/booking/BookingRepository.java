package com.hovedopgave_dat_f25_backend.booking;

import com.hovedopgave_dat_f25_backend.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByFlight(Flight flight);

    @Query("SELECT b FROM Booking b WHERE b.passenger.id = ?1")
    List<Booking> findAllByPassengerId(int passengerId);

    @Query("SELECT b FROM Booking b WHERE b.flight.flightNumber = ?1")
    List<Booking> findAllByFlightNumber(String flightNumber);

    List<Booking> findAllByBookingNumber(String bookingNumber);

    List<Booking> findAllBySeatNumber(String seatNumber);

    List<Booking> findAllByStatus(String status);
}
