package com.btvn.employeemanagement.controller;

import com.btvn.employeemanagement.entity.Employee;
import com.btvn.employeemanagement.exception.NotFoundException;
import com.btvn.employeemanagement.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    // Test case 1: GET /api/employees – trả về HTTP 200 và danh sách JSON.
    @Test
    void testCase1_getAllEmployees_Return200() throws Exception {
        Employee emp = new Employee("Dao Truong Son", "IT", 15000000.0);
        when(employeeService.findAll()).thenReturn(List.of(emp));

        mockMvc.perform(get("/api/v1/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].fullName").value("Dao Truong Son"));
    }

    // Test case 2: GET /api/employees/{id} – trả về HTTP 200 khi tìm thấy.
    @Test
    void testCase2_getById_Found() throws Exception {
        Employee emp = new Employee("Nguyen Van A", "HR", 10000000.0);
        emp.setId(1L);
        when(employeeService.findById(1L)).thenReturn(emp);

        mockMvc.perform(get("/api/v1/employees/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName").value("Nguyen Van A"))
                .andExpect(jsonPath("$.data.department").value("HR"));
    }

    // Test case 3: GET /api/employees/{id} – trả về HTTP 404 khi không tìm thấy.
    @Test
    void testCase3_getById_NotFound() throws Exception {
        when(employeeService.findById(99L)).thenThrow(new NotFoundException("Không tìm thấy nhân viên!"));

        mockMvc.perform(get("/api/v1/employees/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Không tìm thấy nhân viên!"));
    }

    // Test case 4: POST /api/employees – trả về HTTP 201 sau khi tạo thành công.
    @Test
    void testCase4_postEmployee_Success() throws Exception {
        Employee emp = new Employee("Tran Thi B", "Engineering", 20000000.0);
        emp.setId(2L);

        when(employeeService.createEmployee(any())).thenReturn(emp);

        // Sử dụng chuỗi JSON thuần thay cho ObjectMapper
        String jsonBody = """
                {
                    "fullName": "Tran Thi B",
                    "department": "Engineering",
                    "salary": 20000000
                }
                """;

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fullName").value("Tran Thi B"));
    }
}