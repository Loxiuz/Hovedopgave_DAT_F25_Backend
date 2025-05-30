package com.hovedopgave_dat_f25_backend.export_request;

import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.employee.EmployeeService;
import com.hovedopgave_dat_f25_backend.export.ExportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void testGetAllExportRequests() {
        Employee employee1 = new Employee();
        employee1.setId(1);
        Employee employee2 = new Employee();
        employee2.setId(2);
        ExportRequest exportRequest1 = new ExportRequest();
        exportRequest1.setId(1);
        exportRequest1.setEmployee(employee1);
        exportRequest1.setExportCreation(LocalDateTime.now());

        ExportRequest exportRequest2 = new ExportRequest();
        exportRequest2.setId(2);
        exportRequest2.setEmployee(employee2);
        exportRequest2.setExportCreation(LocalDateTime.now());

        when(exportRequestRepository.findAll()).thenReturn(List.of(exportRequest1, exportRequest2));

        ExportRequestDTO[] result = exportRequestService.getAllExportRequests();
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(1, result[0].id());
        assertEquals(2, result[1].id());
    }

    @Test
    void testHandleExportRequestSuccess() {
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1, "csv","", "flight", null, "test.csv", null, "");

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
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "", "flight", null, "test.csv", null, "");

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
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "", null, null, "", "", "");

        assertThrows(IllegalArgumentException.class, () -> exportRequestService.handleExportRequest(exportRequestDTO));
    }

    @Test
    void testHandleExportRequestWithNullSelectedEntities() {
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "", null, null, "test.csv", null, "");

        assertThrows(IllegalArgumentException.class, () -> exportRequestService.handleExportRequest(exportRequestDTO));
    }

    @Test
    void testHandleExportRequestWithInvalidEmployee() {
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO(1,1,"csv", "", "flight", null, "test.csv", null, "");

        when(employeeService.getEmployee(anyInt())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> exportRequestService.handleExportRequest(exportRequestDTO));
    }


}