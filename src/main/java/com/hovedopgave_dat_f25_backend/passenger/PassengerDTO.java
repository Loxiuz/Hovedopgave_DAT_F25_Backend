package com.hovedopgave_dat_f25_backend.passenger;

import java.time.LocalDateTime;

public record PassengerDTO(int id, String name, LocalDateTime birthdate, String nationality) {
}
