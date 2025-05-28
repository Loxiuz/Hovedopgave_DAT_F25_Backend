package com.hovedopgave_dat_f25_backend.export_request;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public record ExportRequestDTO(int id, int employeeId,  String exportFormat, String exportCreation, String selectedEntities, List<JsonNode> appliedFilters, String fileName, String status, String fileSize) {
}
