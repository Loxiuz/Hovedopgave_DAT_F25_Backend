package com.hovedopgave_dat_f25_backend.passenger;

import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
}
