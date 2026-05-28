package com.btvn.employeemanagement.controller;

import com.btvn.employeemanagement.dto.ApiDataResponse;
import com.btvn.employeemanagement.dto.EmployeeDTO;
import com.btvn.employeemanagement.entity.Employee;
import com.btvn.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<List<Employee>>> findAll() {
        log.info(">>> LOGGER : GET /api/v1/employees called");
        List<Employee> employees = employeeService.findAll();
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy danh sách nhân viên thành công!",
                employees,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Employee>> findById(@PathVariable Long id) {
        log.info(">>> LOGGER : GET /api/v1/employees/{} called", id);
        Employee employee = employeeService.findById(id);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy thông tin nhân viên bằng id thành công!",
                employee,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<Employee>> save(@Valid @RequestBody EmployeeDTO dto) {
        log.info(">>> LOGGER : POST /api/v1/employees called with data: {}", dto);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Thêm nhân viên thành công!",
                employeeService.createEmployee(dto),
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Employee>> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        log.info(">>> LOGGER : PUT /api/v1/employees/{} called", id);
        Employee update = employeeService.updateEmployee(dto,id);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Cập nhật toàn bộ thông tin nhân viên thành công!",
                update,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Employee>> delete(@PathVariable Long id) {
        log.info(">>> LOGGER : DELETE /api/v1/employees/{} called", id);
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Xoá nhân viên thành công!",
                null,
                HttpStatus.NO_CONTENT
        ), HttpStatus.NO_CONTENT);
    }
}
