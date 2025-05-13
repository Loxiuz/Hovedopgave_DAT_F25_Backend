package com.hovedopgave_dat_f25_backend.flight;

import com.hovedopgave_dat_f25_backend.airport.AiportRepository;
import com.hovedopgave_dat_f25_backend.airport.Airport;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class FlightRepositoryTest {

    @Autowired
    private AiportRepository aiportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    void testFlightAirportRelation(){
        Airport originAirport = new Airport();
        Airport destinationAirport = new Airport();

        originAirport.setCity("origin");
        originAirport.setCountry("origin");
        aiportRepository.save(originAirport);

        destinationAirport.setCity("destination");
        destinationAirport.setCountry("destination");
        aiportRepository.save(destinationAirport);

        Flight flight = new Flight();
        flight.setAirportOrigin(originAirport);
        flight.setAirportDestination(destinationAirport);
        flight.setFlightNumber("123");
        flightRepository.save(flight);

        Flight[] flights = flightRepository.findFlightByAirportDestination(destinationAirport);
        Assertions.assertEquals(1, flights.length);
    }
}
