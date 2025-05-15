package com.hovedopgave_dat_f25_backend.employee;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee getEmployee(int id) {
        Optional<Employee> e = employeeRepository.findById(id);
        return e.orElse(null);
    }
}
