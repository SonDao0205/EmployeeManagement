package com.btvn.employeemanagement.service.impl;

import com.btvn.employeemanagement.dto.EmployeeDTO;
import com.btvn.employeemanagement.entity.Employee;
import com.btvn.employeemanagement.exception.NotFoundException;
import com.btvn.employeemanagement.repository.EmployeeRepository;
import com.btvn.employeemanagement.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        Employee emp = employeeRepository.findEmployeeById(id);
        if(emp == null){
            logger.warn(">>> LOGGER : Employee not found with id: {}", id);
            throw new NotFoundException("Không tìm thấy nhân viên với id này!");
        }
        return emp;
    }

    @Override
    public Employee createEmployee(EmployeeDTO dto) {
        Employee emp = new Employee(dto.getFullName(),dto.getDepartment(),dto.getSalary());
        return employeeRepository.save(emp);
    }

    @Override
    public Employee updateEmployee(EmployeeDTO dto,Long id) {
        Employee emp = findById(id);
        emp.setFullName(dto.getFullName());
        emp.setDepartment(dto.getDepartment());
        emp.setSalary(dto.getSalary());
        return employeeRepository.update(emp);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee emp = findById(id);
        employeeRepository.delete(emp);
    }
}
