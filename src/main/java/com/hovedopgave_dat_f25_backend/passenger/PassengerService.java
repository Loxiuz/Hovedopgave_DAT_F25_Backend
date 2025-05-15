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
        return passengerRepository.findAll().stream().map(
                passenger -> new PassengerDTO(
                        passenger.getId(),
                        passenger.getName(),
                        passenger.getBirthdate(),
                        passenger.getNationality()

                )
        ).toList();
    }
}
