package com.hovedopgave_dat_f25_backend.passenger;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<PassengerDTO> getAllPassengers() {
        return passengerRepository.findAll().stream().map(
                passenger -> new PassengerDTO(
                        String.valueOf(passenger.getId()),
                        passenger.getNationality()
                )
        ).toList();
    }
}
