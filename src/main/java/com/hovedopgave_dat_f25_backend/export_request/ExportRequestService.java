package com.hovedopgave_dat_f25_backend.export_request;

import org.springframework.stereotype.Service;

@Service
public class ExportRequestService {

    ExportRequestRepository exportRequestRepository;

    public ExportRequestService(ExportRequestRepository exportRequestRepository) {
        this.exportRequestRepository = exportRequestRepository;
    }
}
