package com.hovedopgave_dat_f25_backend.export_request;

import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.employee.EmployeeService;
import com.hovedopgave_dat_f25_backend.export.ExportService;
import com.hovedopgave_dat_f25_backend.flight.FlightDTO;
import com.hovedopgave_dat_f25_backend.flight.FlightService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExportRequestService {

    private final ExportRequestRepository exportRequestRepository;
    private final ExportRequestService exportRequestService;
    private final ExportService exportService;
    private final FlightService flightService;
    private final EmployeeService employeeService;


    public ExportRequestService(ExportRequestRepository exportRequestRepository, ExportRequestService exportRequestService, FlightService flightService, ExportService exportService, EmployeeService employeeService) {
        this.exportRequestRepository = exportRequestRepository;
        this.exportRequestService = exportRequestService;
        this.flightService = flightService;
        this.exportService = exportService;
        this.employeeService = employeeService;
    }

    public byte[] handleExportRequest(ExportRequestDTO exportRequestDTO) {
        ExportRequest ExportRequestFromDto = exportRequestService.fromDTO(exportRequestDTO);
        ExportRequestFromDto.setStatus("PENDING"); // Overvej om det skal være enum
        exportRequestRepository.save(ExportRequestFromDto);

        return handleExport(ExportRequestFromDto);
    }

    public byte[] handleExport(ExportRequest exportRequest) {
        return null;
    }

    public byte[] handleExportFlight(){
        return exportService.exportFlightData(flightService.getFlights(), "csv");
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
