package com.hovedopgave_dat_f25_backend.export_request;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExportRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ExportRequestService exportRequestService;


    @Test
    void exportDataFromEntitiesSuccess() throws Exception {
        when(exportRequestService.handleExportRequest(any())).thenReturn(new byte[0]);

        mockMvc.perform(post("/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"employeeId\":1,\"exportFormat\":\"csv\",\"selectedEntities\":\"flight\",\"fileName\":\"test.csv\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void exportDataFromEntitiesFailure() throws Exception {
        when(exportRequestService.handleExportRequest(any())).thenReturn(null);

        mockMvc.perform(post("/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"employeeId\":1,\"exportFormat\":\"csv\",\"selectedEntities\":\"flight\",\"fileName\":\"test.csv\"}"))
                .andExpect(status().isBadRequest());
    }
}