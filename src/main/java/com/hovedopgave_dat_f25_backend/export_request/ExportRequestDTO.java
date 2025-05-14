package com.hovedopgave_dat_f25_backend.export_request;

import com.hovedopgave_dat_f25_backend.employee.Employee;

import java.time.LocalDate;

public record ExportRequestDTO(int id, Employee employee, LocalDate exportCreation, String exportFormat, String selectedEntities, String appliedFilters, String status, String fileName, String fileSize) {
}
