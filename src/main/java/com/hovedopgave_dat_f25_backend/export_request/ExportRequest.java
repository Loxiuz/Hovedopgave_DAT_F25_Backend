package com.hovedopgave_dat_f25_backend.export_request;
import com.hovedopgave_dat_f25_backend.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

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

    private LocalDate exportCreation;

    private String exportFormat;

    private String selectedEntities;

    private String appliedFilters;

    private String status;

    private String fileName;

    private String fileSize;

}
