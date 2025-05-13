package com.hovedopgave_dat_f25_backend.airport;

import org.springframework.stereotype.Service;

@Service
public class AirportService {

    AiportRepository aiportRepository;

    public AirportService(AiportRepository aiportRepository) {
        this.aiportRepository = aiportRepository;
    }
}
