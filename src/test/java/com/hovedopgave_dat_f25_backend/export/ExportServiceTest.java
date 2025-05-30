package com.hovedopgave_dat_f25_backend.export;

import com.fasterxml.jackson.databind.JsonNode;
import com.hovedopgave_dat_f25_backend.airport.Airport;
import com.hovedopgave_dat_f25_backend.booking.BookingRepository;
import com.hovedopgave_dat_f25_backend.booking.BookingService;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberRepository;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
import com.hovedopgave_dat_f25_backend.crew_member_assignment.CrewMemberAssignmentRepository;
import com.hovedopgave_dat_f25_backend.crew_member_assignment.CrewMemberAssignmentService;
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
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock
    CrewMemberAssignmentRepository crewMemberAssignmentRepository;

    @InjectMocks
    FlightService flightService;
    @InjectMocks
    PassengerService passengerService;
    @InjectMocks
    BookingService bookingService;
    @InjectMocks
    CrewMemberService crewMemberService;
    @InjectMocks
    CrewMemberAssignmentService crewMemberAssignmentService;

    ExportService exportService;

    Airport airport = new Airport();
    Airport airport2 = new Airport();
    Flight flight = new Flight();
    String departureTime = LocalDateTime.now().toString();
    String arrivalTime = LocalDateTime.now().plusHours(2).toString();
    Employee employee = new Employee();
    Passenger passenger = new Passenger();
    LocalDateTime birthdate = LocalDateTime.now();
    List<JsonNode> filters = List.of(new JsonNode[]{mock(JsonNode.class)});

    @BeforeEach
    void setUp() {
        exportService = new ExportService(flightService, passengerService, crewMemberService, bookingService, crewMemberAssignmentService);

        airport.setId(1);
        airport2.setId(2);

        flight.setId(1);
        flight.setFlightNumber("Test Flight");
        flight.setAirportOrigin(airport);
        flight.setAirportDestination(airport2);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);

        passenger.setId(1);
        passenger.setName("John Doe");
        passenger.setNationality("nationality");
        passenger.setBirthdate(birthdate);

        employee.setId(1);
    }

    @Test
    void testProcessExportRequestOneSelectedEntityWithCsv() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setEmployee(employee);
        exportRequest.setSelectedEntities("flight");
        exportRequest.setExportFormat("csv");
        exportRequest.setAppliedFilters(filters);

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        byte[] result = exportService.processExportRequest(exportRequest);
        String expected = "=== FLIGHT ===\n" +
                "flightNumber,departure,arrival\n" +
                "Test Flight," + departureTime + "," + arrivalTime +"\n\n";
        assertEquals(expected, new String(result));
    }

    @Test
    void testProcessExportRequestMultipleSelectedEntitiesWithCsv() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("flight,passenger");
        exportRequest.setExportFormat("csv");
        exportRequest.setAppliedFilters(filters);

        when(flightRepository.findAll()).thenReturn(List.of(flight));
        when(passengerRepository.findAll()).thenReturn(List.of(passenger));

        byte[] result = exportService.processExportRequest(exportRequest);
        String expected = "=== FLIGHT ===\n" +
                "flightNumber,departure,arrival\n" +
                "Test Flight," + departureTime + "," + arrivalTime + "\n\n" +
                "=== PASSENGER ===\n" +
                "passengerId,nationality\n" +
                 passenger.getId() + "," + passenger.getNationality() + "\n\n";
        assertEquals(expected, new String(result));
    }

    @Test
    void testProcessExportRequestOneSelectedEntityWithJson() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("flight");
        exportRequest.setExportFormat("json");
        exportRequest.setAppliedFilters(filters);

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        byte[] result = exportService.processExportRequest(exportRequest);
        String expected = "[\n" +
                "    {\n" +
                "        \"flight\": [\n" +
                "            {\n" +
                "                \"flightNumber\": \"Test Flight\",\n" +
                "                \"departure\": \"" + departureTime + "\",\n" +
                "                \"arrival\": \"" + arrivalTime + "\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";
        assertEquals(expected, new String(result));
    }

    @Test
    void testProcessExportRequestMultipleSelectedEntitiesWithJson() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("flight,passenger");
        exportRequest.setExportFormat("json");
        exportRequest.setAppliedFilters(filters);

        when(flightRepository.findAll()).thenReturn(List.of(flight));
        when(passengerRepository.findAll()).thenReturn(List.of(passenger));

        byte[] result = exportService.processExportRequest(exportRequest);
        String expected = "[\n" +
                "    {\n" +
                "        \"flight\": [\n" +
                "            {\n" +
                "                \"flightNumber\": \"Test Flight\",\n" +
                "                \"departure\": \"" + departureTime + "\",\n" +
                "                \"arrival\": \"" + arrivalTime + "\"\n" +
                "            }\n"+
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"passenger\": [\n" +
                "            {\n" +
                "                \"passengerId\": \"" + passenger.getId() + "\",\n" +
                "                \"nationality\": \"" + passenger.getNationality() + "\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        assertEquals(expected, new String(result));
    }

    @Test
    void testProcessExportRequestEmptySelectedEntities() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("");
        exportRequest.setExportFormat("csv");

       assertThrows(IllegalArgumentException.class, () -> exportService.processExportRequest(exportRequest));
    }

    @Test
    void testProcessExportRequestInvalidExportFormat() {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setSelectedEntities("flight");
        exportRequest.setExportFormat("invalid_format");

        try {
            exportService.processExportRequest(exportRequest);
        } catch (UnsupportedOperationException e) {
            assertEquals("Invalid export format: invalid_format", e.getMessage());
        }
    }
}