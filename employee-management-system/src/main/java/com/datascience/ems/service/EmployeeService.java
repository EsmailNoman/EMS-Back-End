package com.datascience.ems.service;

import com.datascience.ems.dto.EmployeeDTO;
import com.datascience.ems.entity.Department;
import com.datascience.ems.entity.Employee;
import com.datascience.ems.exception.BadRequestException;
import com.datascience.ems.exception.ResourceNotFoundException;
import com.datascience.ems.repository.DepartmentRepository;
import com.datascience.ems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO dto) {

        if (employeeRepository.findByName(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Name already exists: " + dto.getEmail());
        }
        if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists: " + dto.getEmail());
        }
        if (employeeRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new BadRequestException("Phone number already exists: " + dto.getPhone());
        }

        Employee employee = Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .hireDate(dto.getHireDate())
                .salary(dto.getSalary())
                .build();

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(department);
        }

        Employee saved = employeeRepository.save(employee);
        return convertToDTO(saved);
    }


    @Transactional(readOnly = true)
    public Map<String, Object> getEmployeesPaged(int page, int size, String sortBy, Long departmentId) {
        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(page, size,
                        org.springframework.data.domain.Sort.by(sortBy));

        org.springframework.data.domain.Page<Employee> pageResult;

        if (departmentId != null) {
            pageResult = employeeRepository.findByDepartmentId(departmentId, pageable);
        } else {
            pageResult = employeeRepository.findAll(pageable);
        }

        List<EmployeeDTO> employees = pageResult.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("employees", employees);
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return response;
    }



    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return convertToDTO(employee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (!employee.getEmail().equals(dto.getEmail()) &&
                employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists: " + dto.getEmail());
        }
        if (!employee.getPhone().equals(dto.getPhone()) &&
                employeeRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new BadRequestException("Phone number already exists: " + dto.getPhone());
        }

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setHireDate(dto.getHireDate());
        employee.setSalary(dto.getSalary());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        Employee updated = employeeRepository.save(employee);
        return convertToDTO(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .hireDate(employee.getHireDate())
                .salary(employee.getSalary())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .build();
    }
}
