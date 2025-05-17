package com.hovedopgave_dat_f25_backend.export;

import com.hovedopgave_dat_f25_backend.booking.BookingDTO;
import com.hovedopgave_dat_f25_backend.booking.BookingService;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberDTO;
import com.hovedopgave_dat_f25_backend.crew_member.CrewMemberService;
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

    FlightService flightService;
    PassengerService passengerService;
    CrewMemberService crewMemberService;
    BookingService bookingService;

    public ExportService(FlightService flightService, PassengerService passengerService, CrewMemberService crewMemberService, BookingService bookingService) {
        this.flightService = flightService;
        this.passengerService = passengerService;
        this.crewMemberService = crewMemberService;
        this.bookingService = bookingService;
    }

    public byte[] processExportRequest(ExportRequest exportRequest){
        List<String> selectedEntities = Arrays.asList(exportRequest.getSelectedEntities().split(","));
        List<String> appliedFilters = Arrays.asList(exportRequest.getAppliedFilters().split(","));
        String exportFormat = exportRequest.getExportFormat();
        return createExport(selectedEntities, appliedFilters, exportFormat);
    }

    private byte[] createExport(List<String> selectedEntities, List<String> appliedFilters, String exportFormat){
        if(exportFormat.equalsIgnoreCase("csv")){
            return exportToCsv(selectedEntities, appliedFilters);
        } else {
            throw new UnsupportedOperationException("Invalid export format: " + exportFormat);
        }
    }

    private byte[] exportToCsv(List<String> selectedEntities, List<String> appliedFilters) {
        StringBuilder builder = new StringBuilder();

        for (String selectedEntity : selectedEntities) {
            builder.append("=== ").append(selectedEntity.toUpperCase()).append(" ===").append("\n");
            CsvExportStructure<?> structure = buildColumnStructureForEntity(selectedEntity);
            if(structure != null){
                builder.append(structure.columnDataBuilder()).append("\n");
            }
        }

        return builder.toString().getBytes();
    }

    private CsvExportStructure<?> buildColumnStructureForEntity(String selectedEntity) {
        return switch (selectedEntity.toLowerCase()) {
            case "flight" -> {
                List<FlightDTO> flights = flightService.getAllFlights();
                yield new CsvExportStructure<>(
                        flights,
                        List.of("FlightNumber", "Departure", "Arrival"),
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
                        List.of("Birthdate", "name", "nationality"),
                        passengerData -> List.of(
                                passengerData.birthdate().toString(),
                                passengerData.name(),
                                passengerData.nationality()
                        )
                );
            }
            case "crew_member" -> {
                List<CrewMemberDTO> crewMembers = crewMemberService.getAllCrewMembers();
                yield new CsvExportStructure<>(
                        crewMembers,
                        List.of("email", "name", "phoneNumber"),
                        crewMemberData -> List.of(
                                crewMemberData.email(),
                                crewMemberData.name(),
                                crewMemberData.phoneNumber()
                        )
                );
            }
            case "booking" -> {
                List<BookingDTO> bookings = bookingService.getBookings();
                yield new CsvExportStructure<>(
                        bookings,
                        List.of("bookingNumber", "seatNumber", "status", "flightNumber"),
                        bookingData -> List.of(
                                bookingData.bookingNumber(),
                                bookingData.seatNumber(),
                                bookingData.status(),
                                bookingData.flight().getFlightNumber()
                        )
                );
            }
            default -> null;
        };
    }
}
