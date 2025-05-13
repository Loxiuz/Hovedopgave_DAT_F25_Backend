package com.hovedopgave_dat_f25_backend.export_request;


import com.hovedopgave_dat_f25_backend.employee.Employee;
import com.hovedopgave_dat_f25_backend.employee.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
public class ExportRequestController {

    EmployeeRepository employeeRepository;

    public ExportRequestController(EmployeeRepository employeeRepository) {}

    @GetMapping("/employeeId")
    public ResponseEntity<Integer> employeeId(){
        Employee[] employees = employeeRepository.findAll().toArray(new Employee[0]);
        return new ResponseEntity<>(employees.length, HttpStatus.OK);
    }
}
