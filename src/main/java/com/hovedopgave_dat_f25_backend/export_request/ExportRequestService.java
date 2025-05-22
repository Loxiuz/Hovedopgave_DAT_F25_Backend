package com.hovedopgave_dat_f25_backend.export_request;

import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.employee.EmployeeService;
import com.hovedopgave_dat_f25_backend.export.ExportService;
import org.springframework.stereotype.Service;

@Service
public class ExportRequestService {

    private final ExportRequestRepository exportRequestRepository;
    private final ExportService exportService;
    private final EmployeeService employeeService;


    public ExportRequestService(ExportRequestRepository exportRequestRepository, ExportService exportService, EmployeeService employeeService) {
        this.exportRequestRepository = exportRequestRepository;
        this.exportService = exportService;
        this.employeeService = employeeService;
    }

    public byte[] handleExportRequest(ExportRequestDTO exportRequestDTO) {
        ExportRequest exportRequestFromDto = fromDTO(exportRequestDTO);
        exportRequestFromDto.setStatus("PENDING"); // Overvej om det skal være enum
        //Håndter fejl ved tom selectedEntities (og opdater test)
        if (exportRequestFromDto.getSelectedEntities() == null || exportRequestFromDto.getSelectedEntities().isEmpty()) {
            throw new IllegalArgumentException("Selected entities cannot be null or empty");
        } else {
            exportRequestRepository.save(exportRequestFromDto);

            byte[] exportOutput = exportService.processExportRequest(exportRequestFromDto);

           if(exportOutput != null && exportOutput.length > 0) {
               String fileSize = String.valueOf(exportOutput.length);
               exportRequestFromDto.setFileSize(fileSize);
                exportRequestFromDto.setStatus("COMPLETED");
               exportRequestRepository.save(exportRequestFromDto); //Muligvis forbedre opdatering af felt

               return exportOutput;
           }
              else {
                exportRequestFromDto.setStatus("FAILED");
                exportRequestRepository.save(exportRequestFromDto);
                throw new RuntimeException("Export failed");
              }
        }
    }

    private ExportRequest fromDTO(ExportRequestDTO exportRequestDTO) {
        ExportRequest exportRequest = new ExportRequest();
        Employee employee = employeeService.getEmployee(exportRequestDTO.employeeId());
        if(employee == null) {
            throw new IllegalArgumentException("Employee from ExportRequestDTO not found");
        }
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
