package com.hovedopgave_dat_f25_backend.export_request;

public record ExportRequestDTO(int id, int employeeId, String exportFormat, String selectedEntities, String appliedFilters, String fileName) {
}
