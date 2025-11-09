package com.datascience.ems.service;


import com.datascience.ems.dto.EmployeeProjectDTO;
import com.datascience.ems.entity.Employee;
import com.datascience.ems.entity.EmployeeProject;
import com.datascience.ems.entity.Project;
import com.datascience.ems.exception.BadRequestException;
import com.datascience.ems.exception.ResourceNotFoundException;
import com.datascience.ems.repository.EmployeeProjectRepository;
import com.datascience.ems.repository.EmployeeRepository;
import com.datascience.ems.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeProjectService {

    private final EmployeeProjectRepository employeeProjectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public EmployeeProjectDTO assignEmployeeToProject(EmployeeProjectDTO dto) {
        if (employeeProjectRepository.findByEmployeeIdAndProjectId(
                dto.getEmployeeId(), dto.getProjectId()).isPresent()) {
            throw new BadRequestException("Employee already assigned to this project");
        }

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        EmployeeProject employeeProject = EmployeeProject.builder()
                .employee(employee)
                .project(project)
                .role(dto.getRole())
                .build();

        EmployeeProject saved = employeeProjectRepository.save(employeeProject);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<EmployeeProjectDTO> getProjectsByEmployee(Long employeeId) {
        return employeeProjectRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeProjectDTO> getEmployeesByProject(Long projectId) {
        return employeeProjectRepository.findByProjectId(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeProjectDTO updateEmployeeProjectRole(Long id, String role) {
        EmployeeProject employeeProject = employeeProjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        employeeProject.setRole(role);
        EmployeeProject updated = employeeProjectRepository.save(employeeProject);
        return convertToDTO(updated);
    }

    @Transactional
    public void removeEmployeeFromProject(Long id) {
        EmployeeProject employeeProject = employeeProjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
        employeeProjectRepository.delete(employeeProject);
    }

    private EmployeeProjectDTO convertToDTO(EmployeeProject ep) {
        EmployeeProjectDTO dto = new EmployeeProjectDTO();
        dto.setId(ep.getId());
        dto.setEmployeeId(ep.getEmployee().getId());
        dto.setEmployeeName(ep.getEmployee().getName());
        dto.setProjectId(ep.getProject().getId());
        dto.setProjectName(ep.getProject().getName());
        dto.setRole(ep.getRole());
        return dto;
    }
}
