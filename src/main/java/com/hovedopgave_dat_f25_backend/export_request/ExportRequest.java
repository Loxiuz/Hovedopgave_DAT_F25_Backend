package com.hovedopgave_dat_f25_backend.export_request;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hovedopgave_dat_f25_backend.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Employee employee;

    @CreationTimestamp
    private LocalDateTime exportCreation;

    @Lob
    private String appliedFiltersJson;

    @Transient
    private List<JsonNode> appliedFilters;

    private String exportFormat;

    private String selectedEntities;

    private String status;

    private String fileName;

    private String fileSize;

    public void setAppliedFilters(List<JsonNode> filters) {
        this.appliedFilters = filters;
        try {
            this.appliedFiltersJson = new ObjectMapper().writeValueAsString(filters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<JsonNode> getAppliedFilters() {
        if (appliedFilters == null && appliedFiltersJson != null) {
            try {
                appliedFilters = new ObjectMapper().readValue(appliedFiltersJson,
                        new ObjectMapper().getTypeFactory().constructCollectionType(List.class, JsonNode.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return appliedFilters;
    }

}
