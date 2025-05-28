package com.hovedopgave_dat_f25_backend.employee;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDTO getEmployee(int id) {
        Optional<Employee> e = employeeRepository.findById(id);
    if (e.isPresent()) {
            Employee employee = e.get();
            return new EmployeeDTO(
                    employee.getId(),
                    employee.getName(),
                    employee.getEmail()
            );
    } else {
            return null;
        }
    }
}
