package com.hovedopgave_dat_f25_backend.export;

import com.fasterxml.jackson.databind.JsonNode;
import com.hovedopgave_dat_f25_backend.booking.BookingDTO;
import com.hovedopgave_dat_f25_backend.booking.BookingService;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberDTO;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
import com.hovedopgave_dat_f25_backend.crew_member_assignment.CrewMemberAssignmentDTO;
import com.hovedopgave_dat_f25_backend.crew_member_assignment.CrewMemberAssignmentService;
import com.hovedopgave_dat_f25_backend.export_request.ExportRequest;
import com.hovedopgave_dat_f25_backend.flight.FlightDTO;
import com.hovedopgave_dat_f25_backend.flight.FlightService;
import com.hovedopgave_dat_f25_backend.passenger.PassengerDTO;
import com.hovedopgave_dat_f25_backend.passenger.PassengerService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ExportService {

    private final CrewMemberAssignmentService crewMemberAssignmentService;
    private final FlightService flightService;
    private final PassengerService passengerService;
    private final CrewMemberService crewMemberService;
    private final BookingService bookingService;

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
            return exportToCsv(selectedEntities, appliedFilters, exportFormat);
        } else if (exportFormat.equalsIgnoreCase("json")) {
            return exportToJson(selectedEntities, appliedFilters, exportFormat);
        }
        else {
            throw new UnsupportedOperationException("Invalid export format: " + exportFormat);
        }
    }

    private byte[] exportToCsv(List<String> selectedEntities, List<JsonNode> appliedFilters, String exportFormat) {
        StringBuilder builder = new StringBuilder();

        for (String selectedEntity : selectedEntities) {
            builder.append("=== ").append(selectedEntity.toUpperCase()).append(" ===").append("\n");
            ExportStructure structure = buildExportStructureForEntity(selectedEntity, appliedFilters, exportFormat);
            if(structure != null){
                builder.append(structure.buildData()).append("\n");
            }
        }

        return builder.toString().getBytes();
    }

    private byte[] exportToJson(List<String> selectedEntities, List<JsonNode> appliedFilters, String exportFormat) {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");

        for (int i = 0; i < selectedEntities.size(); i++) {
            String selectedEntity = selectedEntities.get(i);
            builder.append("    {\n");
            builder.append("        \"").append(selectedEntity.toLowerCase()).append("\": [\n");
            ExportStructure structure = buildExportStructureForEntity(selectedEntity, appliedFilters, exportFormat);
            if(structure != null){
                builder.append(structure.buildData());
            }
            builder.append("        ]\n");
            builder.append("    }");
            if (i < selectedEntities.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }

        builder.append("]");
        return builder.toString().getBytes();
    }

    private ExportStructure buildExportStructureForEntity(String selectedEntity, List<JsonNode> appliedFilters, String exportFormat) {
        return switch (selectedEntity.toLowerCase()) {
            case "flight" -> {
                List<FlightDTO> flights = flightService.getFilteredFlights(
                        appliedFilters.stream().filter(
                                filter -> filter.has("flight")
                        ).toList());
                if(exportFormat.equalsIgnoreCase("csv")){
                    yield new CsvExportStructure<>(
                            flights,
                            List.of("flightNumber", "departure", "arrival"),
                            flightData -> List.of(
                                    flightData.flightNumber(),
                                    flightData.departureTime(),
                                    flightData.arrivalTime()
                            )
                    );
                } else if (exportFormat.equalsIgnoreCase("json")) {
                    yield new JsonExportStructure<>(
                            flights,
                            List.of("flightNumber", "departure", "arrival"),
                            flightData -> List.of(
                                    flightData.flightNumber(),
                                    flightData.departureTime(),
                                    flightData.arrivalTime()
                            )
                    );
                } else {
                    yield null;
                }
            }
            case "passenger" -> {
                List<PassengerDTO> passengers = passengerService.getAllPassengers();
                if(exportFormat.equalsIgnoreCase("csv")){
                    yield new CsvExportStructure<>(
                            passengers,
                            List.of("passengerId", "nationality"),
                            passengerData -> List.of(
                                    passengerData.id(),
                                    passengerData.nationality()
                            )
                    );
                } else if (exportFormat.equalsIgnoreCase("json")) {
                    yield new JsonExportStructure<>(
                            passengers,
                            List.of("passengerId", "nationality"),
                            passengerData -> List.of(
                                    passengerData.id(),
                                    passengerData.nationality()
                            )
                    );
                } else {
                    yield null;
                }
            }
            case "crew_member" -> {
                List<CrewMemberDTO> crewMembers = crewMemberService.getFilteredCrewMembers(
                        appliedFilters.stream().filter(
                                filter -> filter.has("crew_member")
                        ).toList()
                );
                if(exportFormat.equalsIgnoreCase("csv")){
                    yield new CsvExportStructure<>(
                            crewMembers,
                            List.of("crewMemberId", "name", "email"),
                            crewMemberData -> List.of(
                                    crewMemberData.id(),
                                    crewMemberData.name(),
                                    crewMemberData.email()
                            )
                    );
                } else if (exportFormat.equalsIgnoreCase("json")) {
                    yield new JsonExportStructure<>(
                            crewMembers,
                            List.of("crewMemberId", "name", "email"),
                            crewMemberData -> List.of(
                                    crewMemberData.id(),
                                    crewMemberData.name(),
                                    crewMemberData.email()
                            )
                    );
                } else {
                    yield null;
                }
            }
            case "crew_member_assignment" -> {
                List<CrewMemberAssignmentDTO> crewMemberAssignments = crewMemberAssignmentService.getFilteredCrewMemberAssignments(
                        appliedFilters.stream().filter(
                                filter -> filter.has("crew_member_assignment")
                        ).toList());
                if (exportFormat.equalsIgnoreCase("csv")) {
                    yield new CsvExportStructure<>(
                            crewMemberAssignments,
                            List.of("crewMemberId", "role", "flightNumber"),
                            crewMemberData -> List.of(
                                    crewMemberData.crewMemberId(),
                                    crewMemberData.role(),
                                    crewMemberData.flightNumber()
                            )
                    );
                } else if (exportFormat.equalsIgnoreCase("json")) {
                    yield new JsonExportStructure<>(
                            crewMemberAssignments,
                            List.of("crewMemberId", "role", "flightNumber"),
                            crewMemberData -> List.of(
                                    crewMemberData.crewMemberId(),
                                    crewMemberData.role(),
                                    crewMemberData.flightNumber()
                            )
                    );
                } else {
                    yield null;
                }
            }
            case "booking" -> {
                List<BookingDTO> bookings = bookingService.getFilteredBookings(
                       appliedFilters.stream().filter(
                                filter -> filter.has("booking")
                       ).toList());
                if(exportFormat.equalsIgnoreCase("csv")){
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
                } else if (exportFormat.equalsIgnoreCase("json")) {
                    yield new JsonExportStructure<>(
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
                } else {
                    yield null;
                }
            }
            default -> null;
        };
    }
}
