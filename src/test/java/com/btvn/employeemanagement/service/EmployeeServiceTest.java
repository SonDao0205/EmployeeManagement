package com.btvn.employeemanagement.service;

import com.btvn.employeemanagement.dto.EmployeeDTO;
import com.btvn.employeemanagement.entity.Employee;
import com.btvn.employeemanagement.exception.NotFoundException;
import com.btvn.employeemanagement.repository.EmployeeRepository;
import com.btvn.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee("Dao Truong Son", "IT", 20000000.0);
        sampleEmployee.setId(1L);
    }

    @Test
    void getAllEmployees_ReturnList() {
        when(employeeRepository.findAll()).thenReturn(List.of(sampleEmployee));
        List<Employee> result = employeeService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getById_Found() {
        when(employeeRepository.findEmployeeById(1L)).thenReturn(sampleEmployee);
        Employee result = employeeService.findById(1L);
        assertNotNull(result);
        assertEquals("Dao Truong Son", result.getFullName());
    }

    @Test
    void getById_NotFound_ThrowException() {
        when(employeeRepository.findEmployeeById(99L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> employeeService.findById(99L));
    }

    @Test
    void addEmployee_Success() {
        EmployeeDTO dto = new EmployeeDTO("Son Dao", "IT", 15000000.0);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee saved = employeeService.createEmployee(dto);
        assertNotNull(saved);
        assertEquals("Son Dao", saved.getFullName());
    }

    @Test
    void deleteEmployee_RemovesCorrectElement() {
        when(employeeRepository.findEmployeeById(1L)).thenReturn(sampleEmployee);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(sampleEmployee);
    }
}