package com.hovedopgave_dat_f25_backend.export_request;

import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.employee.EmployeeService;
import com.hovedopgave_dat_f25_backend.export.ExportService;
import com.hovedopgave_dat_f25_backend.flight.FlightDTO;
import com.hovedopgave_dat_f25_backend.flight.FlightService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportRequestService {

    private final ExportRequestRepository exportRequestRepository;
    private final FlightService flightService;
    private final ExportService exportService;
    private final EmployeeService employeeService;


    public ExportRequestService(ExportRequestRepository exportRequestRepository, FlightService flightService, ExportService exportService, EmployeeService employeeService) {
        this.exportRequestRepository = exportRequestRepository;
        this.flightService = flightService;
        this.exportService = exportService;
        this.employeeService = employeeService;
    }

    public byte[] handleExportRequest(ExportRequest exportRequest) {
        exportRequest.setStatus("PENDING"); // Overvej om det skal være enum
        exportRequestRepository.save(exportRequest);
        return handleExportFlight();
    }

    public byte[] handleExportFlight(){
        //Dette burde nok blive rykket til flight service(og det samme for andre entiteter med samme mapping),
        //så metoderne i flightService kan returnere dto i stedet for entitet.
        List<FlightDTO> dtos = flightService.getFlights()
                .stream()
                .map(flight -> new FlightDTO(
                        flight.getId(),
                        flight.getAirportOrigin(),
                        flight.getAirportDestination(),
                        flight.getFlightNumber(),
                        flight.getDepartureTime(),
                        flight.getArrivalTime()))
                .toList();
        return exportService.exportFlightData(dtos, "csv");
    }

    public ExportRequest fromDTO(ExportRequestDTO exportRequestDTO) {
        ExportRequest exportRequest = new ExportRequest();
        Employee employee = employeeService.getEmployee(exportRequestDTO.employeeId());

        exportRequest.setEmployee(employee);
        exportRequest.setExportFormat(exportRequestDTO.exportFormat());
        exportRequest.setSelectedEntities(exportRequestDTO.selectedEntities());
        exportRequest.setAppliedFilters(exportRequestDTO.appliedFilters());
        exportRequest.setFileName(exportRequestDTO.fileName());

        return exportRequest;
    }

//    public ExportRequestDTO toDTO(ExportRequest er) {
//        return new ExportRequestDTO(er.getId(), er.getEmployee(),er.getExportCreation(),er.getExportFormat(),er.getSelectedEntities(),er.getAppliedFilters(),er.getExportFormat(),er.getFileName(), er.getFileSize());
//    }
}
