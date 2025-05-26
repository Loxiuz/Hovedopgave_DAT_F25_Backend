package com.hovedopgave_dat_f25_backend.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hovedopgave_dat_f25_backend.booking.BookingDTO;
import com.hovedopgave_dat_f25_backend.booking.BookingService;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberDTO;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
import com.hovedopgave_dat_f25_backend.crew_member_assignment.CrewMemberAssignmentService;
import com.hovedopgave_dat_f25_backend.export_request.ExportRequest;
import com.hovedopgave_dat_f25_backend.flight.FlightDTO;
import com.hovedopgave_dat_f25_backend.flight.FlightService;
import com.hovedopgave_dat_f25_backend.passenger.PassengerDTO;
import com.hovedopgave_dat_f25_backend.passenger.PassengerService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ExportService {

    private final CrewMemberAssignmentService crewMemberAssignmentService;
    FlightService flightService;
    PassengerService passengerService;
    CrewMemberService crewMemberService;
    BookingService bookingService;

    public ExportService(FlightService flightService, PassengerService passengerService, CrewMemberService crewMemberService, BookingService bookingService, CrewMemberAssignmentService crewMemberAssignmentService) {
        this.flightService = flightService;
        this.passengerService = passengerService;
        this.crewMemberService = crewMemberService;
        this.bookingService = bookingService;
        this.crewMemberAssignmentService = crewMemberAssignmentService;
    }

    public byte[] processExportRequest(ExportRequest exportRequest){
        List<String> selectedEntities = Arrays.asList(exportRequest.getSelectedEntities().split(","));
        if(selectedEntities.isEmpty() || selectedEntities.get(0).isEmpty() ){
            throw new IllegalArgumentException("Selected entities cannot be empty");
        }
        List<JsonNode> appliedFilters = exportRequest.getAppliedFilters();
        String exportFormat = exportRequest.getExportFormat();
        return createExport(selectedEntities, appliedFilters, exportFormat);
    }

    private byte[] createExport(List<String> selectedEntities, List<JsonNode> appliedFilters, String exportFormat){
        if(exportFormat.equalsIgnoreCase("csv")){
            return exportToCsv(selectedEntities, appliedFilters);
        } else {
            throw new UnsupportedOperationException("Invalid export format: " + exportFormat);
        }
    }

    private byte[] exportToCsv(List<String> selectedEntities, List<JsonNode> appliedFilters) {
        StringBuilder builder = new StringBuilder();

        for (String selectedEntity : selectedEntities) {
            builder.append("=== ").append(selectedEntity.toUpperCase()).append(" ===").append("\n");
            CsvExportStructure<?> structure = buildCsvColumnStructureForEntity(selectedEntity, appliedFilters);
            if(structure != null){
                builder.append(structure.buildColumnData()).append("\n");
            }
        }

        return builder.toString().getBytes();
    }

    private CsvExportStructure<?> buildCsvColumnStructureForEntity(String selectedEntity, List<JsonNode> appliedFilters) {
        return switch (selectedEntity.toLowerCase()) {
            case "flight" -> {
                List<FlightDTO> flights = flightService.getFilteredFlights(
                        appliedFilters.stream().filter(
                                filter -> filter.has("flight")
                        ).toList());
                yield new CsvExportStructure<>(
                        flights,
                        List.of("flightNumber", "departure", "arrival"),
                        flightData -> List.of(
                                flightData.flightNumber(),
                                flightData.departureTime(),
                                flightData.arrivalTime()
                        )
                );
            }
            case "passenger" -> {
                List<PassengerDTO> passengers = passengerService.getAllPassengers();
                yield new CsvExportStructure<>(
                        passengers,
                        List.of("id"),
                        passengerData -> List.of(
                                passengerData.id()
                        )
                );
            }
            case "crew_member" -> {
                List<CrewMemberDTO> crewMembers = crewMemberAssignmentService.getFilteredCrewMembers(
                        appliedFilters.stream().filter(
                                filter -> filter.has("crew_member")
                        ).toList());
                yield new CsvExportStructure<>(
                        crewMembers,
                        List.of("crewMemberId"),
                        crewMemberData -> List.of(
                               crewMemberData.id()
                        )
                );
            }
            case "booking" -> {
                List<BookingDTO> bookings = bookingService.getFilteredBookings(
                       appliedFilters.stream().filter(
                                filter -> filter.has("booking")
                       ).toList());
                yield new CsvExportStructure<>(
                        bookings,
                        List.of("flightNumber","bookingNumber", "seatNumber", "status", "passengerId"),
                        bookingData -> List.of(
                                bookingData.flightNumber(),
                                bookingData.bookingNumber(),
                                bookingData.seatNumber(),
                                bookingData.status(),
                                bookingData.passengerId()
                        )
                );
            }
            default -> null;
        };
    }
}
