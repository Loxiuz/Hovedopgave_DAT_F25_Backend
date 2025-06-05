package com.hovedopgave_dat_f25_backend.passenger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<PassengerDTO> getAllPassengers() {
        return passengerRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private PassengerDTO toDto(Passenger passenger) {
        return new PassengerDTO(
                String.valueOf(passenger.getId()),
                passenger.getNationality()
        );
    }
}
