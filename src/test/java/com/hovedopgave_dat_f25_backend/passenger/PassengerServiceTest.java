package com.hovedopgave_dat_f25_backend.passenger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    PassengerRepository passengerRepository;

    @InjectMocks
    PassengerService passengerService;

    @Test
    void getAllPassengers() {
        Passenger passenger1 = new Passenger();
        passenger1.setId(1);
        Passenger passenger2 = new Passenger();
        passenger2.setId(2);

        when(passengerRepository.findAll()).thenReturn(List.of(passenger1, passenger2));
        List<PassengerDTO> result = passengerService.getAllPassengers();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("2", result.get(1).id());
    }
}