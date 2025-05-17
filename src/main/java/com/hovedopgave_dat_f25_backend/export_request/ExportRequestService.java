package com.hovedopgave_dat_f25_backend.export_request;

import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.employee.EmployeeService;
import com.hovedopgave_dat_f25_backend.export.ExportService;
import org.springframework.stereotype.Service;

@Service
public class ExportRequestService {

    private final ExportRequestRepository exportRequestRepository;
    private final ExportRequestService exportRequestService;
    private final ExportService exportService;
    private final EmployeeService employeeService;


    public ExportRequestService(ExportRequestRepository exportRequestRepository, ExportRequestService exportRequestService, ExportService exportService, EmployeeService employeeService) {
        this.exportRequestRepository = exportRequestRepository;
        this.exportRequestService = exportRequestService;
        this.exportService = exportService;
        this.employeeService = employeeService;
    }

    public byte[] handleExportRequest(ExportRequestDTO exportRequestDTO) {
        ExportRequest ExportRequestFromDto = exportRequestService.fromDTO(exportRequestDTO);
        ExportRequestFromDto.setStatus("PENDING"); // Overvej om det skal være enum
        //Håndter fejl ved tom selectedEntities
        exportRequestRepository.save(ExportRequestFromDto);

        return exportService.processExportRequest(ExportRequestFromDto);
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
