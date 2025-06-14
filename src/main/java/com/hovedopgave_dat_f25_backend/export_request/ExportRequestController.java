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

    @PostMapping
    public ResponseEntity<byte[]> exportDataFromEntities(@RequestBody ExportRequestDTO exportRequestDTO){
        byte[] fileBytes = exportRequestService.handleExportRequest(exportRequestDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDisposition(ContentDisposition.attachment().filename(exportRequestDTO.fileName()).build());

        if (fileBytes != null) {
            headers.setContentLength(fileBytes.length);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/all-requests")
    public ResponseEntity<ExportRequestDTO[]> getAllExportRequests() {
        ExportRequestDTO[] exportRequests = exportRequestService.getAllExportRequests();
        if (exportRequests != null && exportRequests.length > 0) {
            return new ResponseEntity<>(exportRequests, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
