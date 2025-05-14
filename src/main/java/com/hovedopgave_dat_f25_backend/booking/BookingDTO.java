package com.hovedopgave_dat_f25_backend.booking;

import com.hovedopgave_dat_f25_backend.flight.Flight;
import com.hovedopgave_dat_f25_backend.passenger.Passenger;

public record BookingDTO(int id, Passenger passenger, Flight flight, String bookingNumber, String seatNumber, String status) {
}
