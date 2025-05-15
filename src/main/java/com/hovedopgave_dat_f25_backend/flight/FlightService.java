package com.hovedopgave_dat_f25_backend.flight;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getFlights() {
    return flightRepository.findAll();
    }
}
