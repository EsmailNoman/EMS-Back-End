package com.datascience.ems.service;

import com.datascience.ems.dto.DepartmentDTO;
import com.datascience.ems.entity.Department;
import com.datascience.ems.exception.BadRequestException;
import com.datascience.ems.exception.ResourceNotFoundException;
import com.datascience.ems.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO dto) {

        if (departmentRepository.findByName(dto.getName()).isPresent()) {
            throw new BadRequestException("Department with name '" + dto.getName() + "' already exists");
        }
        Department department = Department.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .budget(dto.getBudget())
                .build();

        Department saved = departmentRepository.save(department);
        return convertToDTO(saved);
    }


    @Transactional(readOnly = true)
    public Map<String, Object> getDepartmentsPaged(int page, int size, String sortBy) {
        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(page, size,
                        org.springframework.data.domain.Sort.by(sortBy));

        org.springframework.data.domain.Page<Department> pageResult = departmentRepository.findAll(pageable);

        List<DepartmentDTO> departments = pageResult.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("departments", departments);
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return response;
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return convertToDTO(department);
    }

    @Transactional
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        department.setName(dto.getName());
        department.setLocation(dto.getLocation());
        department.setBudget(dto.getBudget());

        Department updated = departmentRepository.save(department);
        return convertToDTO(updated);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (!department.getEmployees().isEmpty()) {
            throw new BadRequestException("Cannot delete department with assigned employees");
        }

        departmentRepository.delete(department);
    }

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());
        dto.setBudget(department.getBudget());
        dto.setEmployeeCount(department.getEmployees() != null ? department.getEmployees().size() : 0);    // ← ADDED NULL CHECK
        dto.setProjectCount(department.getProjects() != null ? department.getProjects().size() : 0);      // ← ADDED NULL CHECK
        return dto;
    }
}