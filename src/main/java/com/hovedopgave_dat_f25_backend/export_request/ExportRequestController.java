package com.hovedopgave_dat_f25_backend.export_request;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/export")
public class ExportRequestController {

    ExportRequestService exportRequestService;

    public ExportRequestController(ExportRequestService exportRequestService) {
        this.exportRequestService = exportRequestService;
    }

    @PostMapping("/flights")
    public ResponseEntity<byte[]> exportFlights(@RequestBody ExportRequestDTO exportRequestDTO){
        ExportRequest ExportRequestFromDto = exportRequestService.fromDTO(exportRequestDTO);
        byte[] fileBytes = exportRequestService.handleExportRequest(ExportRequestFromDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDisposition(ContentDisposition.attachment().filename(exportRequestDTO.fileName()).build());

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
}
