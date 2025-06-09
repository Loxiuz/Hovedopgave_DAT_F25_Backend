package com.hovedopgave_dat_f25_backend.booking;

import com.hovedopgave_dat_f25_backend.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByFlight(Flight flight);

    @Query("SELECT b FROM Booking b WHERE " +
           "(:passengerId IS NULL OR b.passenger.id = :passengerId) AND " +
           "(:flightNumber IS NULL OR b.flight.flightNumber = :flightNumber) AND " +
           "(:bookingNumber IS NULL OR b.bookingNumber = :bookingNumber) AND " +
           "(:seatNumber IS NULL OR b.seatNumber = :seatNumber) AND " +
           "(:status IS NULL OR b.status = :status)")
    List<Booking> findAllByFields(
            @Param("passengerId") String passengerId,
            @Param("flightNumber") String flightNumber,
            @Param("bookingNumber") String bookingNumber,
            @Param("seatNumber") String seatNumber,
            @Param("status") String status
    );
}
