package com.hovedopgave_dat_f25_backend.export;

import com.hovedopgave_dat_f25_backend.airport.Airport;
import com.hovedopgave_dat_f25_backend.booking.BookingRepository;
import com.hovedopgave_dat_f25_backend.booking.BookingService;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberRepository;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.export_request.ExportRequest;
import com.hovedopgave_dat_f25_backend.flight.Flight;
import com.hovedopgave_dat_f25_backend.flight.FlightRepository;
import com.hovedopgave_dat_f25_backend.flight.FlightService;
import com.hovedopgave_dat_f25_backend.passenger.Passenger;
import com.hovedopgave_dat_f25_backend.passenger.PassengerRepository;
import com.hovedopgave_dat_f25_backend.passenger.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    @Mock
    FlightRepository flightRepository;
    @Mock
    PassengerRepository passengerRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CrewMemberRepository crewMemberRepository;

    @InjectMocks
    FlightService flightService;
    @InjectMocks
    PassengerService passengerService;
    @InjectMocks
    BookingService bookingService;
    @InjectMocks
    CrewMemberService crewMemberService;

    ExportService exportService;

    @BeforeEach
    void setUp() {
        exportService = new ExportService(flightService, passengerService, crewMemberService, bookingService);
    }

    @Test
    void testProcessExportRequestOneSelectedEntity() {

        Airport airport = new Airport();
        airport.setId(1);
        Airport airport2 = new Airport();
        airport2.setId(2);

        Flight flight = new Flight();
        flight.setId(1);
        flight.setFlightNumber("Test Flight");
        flight.setAirportOrigin(airport);
        flight.setAirportDestination(airport2);

        String departureTime = LocalDateTime.now().toString();
        String arrivalTime = LocalDateTime.now().plusHours(2).toString();

        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        Employee employee = new Employee();
        employee.setId(1);

        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setEmployee(employee);
        exportRequest.setSelectedEntities("flight");
        exportRequest.setAppliedFilters("all");
        exportRequest.setExportFormat("csv");

        byte[] result = exportService.processExportRequest(exportRequest);
        String expected = "=== FLIGHT ===\n" +
                "flightNumber,departure,arrival\n" +
                "Test Flight," + departureTime + "," + arrivalTime +"\n\n";
        assertEquals(expected, new String(result));
    }

    @Test
    void testProcessExportRequestMultipleSelectedEntities() {
        Airport airport = new Airport();
        airport.setId(1);
        Airport airport2 = new Airport();
        airport2.setId(2);

        Flight flight = new Flight();
        flight.setId(1);
        flight.setFlightNumber("Test Flight");
        flight.setAirportOrigin(airport);
        flight.setAirportDestination(airport2);

        String departureTime = LocalDateTime.now().toString();
        String arrivalTime = LocalDateTime.now().plusHours(2).toString();
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);

        Passenger passenger = new Passenger();
        passenger.setId(1);
        passenger.setName("John Doe");
        passenger.setNationality("nationality");

        LocalDateTime birthdate = LocalDateTime.now();
        passenger.setBirthdate(birthdate);

        when(flightRepository.findAll()).thenReturn(List.of(flight));
        when(passengerRepository.findAll()).thenReturn(List.of(passenger));

        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("flight,passenger");
        exportRequest.setAppliedFilters("all");
        exportRequest.setExportFormat("csv");

        byte[] result = exportService.processExportRequest(exportRequest);
        String expected = "=== FLIGHT ===\n" +
                "flightNumber,departure,arrival\n" +
                "Test Flight," + departureTime + "," + arrivalTime + "\n\n" +
                "=== PASSENGER ===\n" +
                "birthdate,name,nationality\n" +
                 birthdate + ",John Doe,nationality\n\n";
        assertEquals(expected, new String(result));
    }

    @Test
    void testProcessExportRequestEmptySelectedEntities() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("");
        exportRequest.setAppliedFilters("all");
        exportRequest.setExportFormat("csv");

       assertThrows(IllegalArgumentException.class, () -> {
            exportService.processExportRequest(exportRequest);
        });
    }

    @Test
    void testProcessExportRequestInvalidExportFormat() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("flight");
        exportRequest.setAppliedFilters("all");
        exportRequest.setExportFormat("invalid_format");

        try {
            exportService.processExportRequest(exportRequest);
        } catch (UnsupportedOperationException e) {
            assertEquals("Invalid export format: invalid_format", e.getMessage());
        }
    }
}