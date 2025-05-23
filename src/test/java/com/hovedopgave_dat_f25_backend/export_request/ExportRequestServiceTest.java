package com.hovedopgave_dat_f25_backend.export_request;

import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.employee.EmployeeService;
import com.hovedopgave_dat_f25_backend.export.ExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExportRequestServiceTest {

    @Mock
    private ExportRequestRepository exportRequestRepository;
    @Mock
    private ExportService exportService;
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private ExportRequestService exportRequestService;

    @Test
    void testHandleExportRequestSuccess() {
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "flight", "", "test.csv");

        Employee employee = new Employee();
        employee.setId(1);

        ExportRequest saved = new ExportRequest();
        saved.setId(1);
        saved.setExportFormat("csv");
        saved.setStatus("COMPLETED");

        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(exportService.processExportRequest(any())).thenReturn("test".getBytes());
        when(exportRequestRepository.findById(1)).thenReturn(Optional.of(saved));

        byte[] result = exportRequestService.handleExportRequest(exportRequestDTO);
        assertNotNull(result);

        Optional<ExportRequest> savedRequest = exportRequestRepository.findById(1);
        assertTrue(savedRequest.isPresent());
        assertEquals("csv", savedRequest.get().getExportFormat());
        assertEquals("COMPLETED", savedRequest.get().getStatus());
    }

    @Test
    void testHandleExportRequestFailed(){
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "flight", "", "test.csv");

        Employee employee = new Employee();
        employee.setId(1);

        ExportRequest saved = new ExportRequest();
        saved.setId(1);
        saved.setExportFormat("csv");
        saved.setStatus("FAILED");

        when(employeeService.getEmployee(anyInt())).thenReturn(employee);
        when(exportService.processExportRequest(any())).thenReturn(null);
        when(exportRequestRepository.findById(1)).thenReturn(Optional.of(saved));

        assertThrows(RuntimeException.class, () -> exportRequestService.handleExportRequest(exportRequestDTO));
        Optional<ExportRequest> savedRequest = exportRequestRepository.findById(1);
        assertTrue(savedRequest.isPresent());
        assertEquals("FAILED", savedRequest.get().getStatus());
    }

    @Test
    void testHandleExportRequestWithEmptySelectedEntities() {
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "", "", "test.csv");

        assertThrows(IllegalArgumentException.class, () -> exportRequestService.handleExportRequest(exportRequestDTO));
    }

    @Test
    void testHandleExportRequestWithNullSelectedEntities() {
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", null, "", "test.csv");

        assertThrows(IllegalArgumentException.class, () -> exportRequestService.handleExportRequest(exportRequestDTO));
    }

    @Test
    void testHandleExportRequestWithInvalidEmployee() {
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "flight", "", "test.csv");

        assertThrows(IllegalArgumentException.class, () -> exportRequestService.handleExportRequest(exportRequestDTO));
    }
}