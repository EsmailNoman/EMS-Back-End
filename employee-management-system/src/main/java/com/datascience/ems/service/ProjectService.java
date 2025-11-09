package com.datascience.ems.service;

import com.datascience.ems.dto.ProjectDTO;
import com.datascience.ems.entity.Department;
import com.datascience.ems.entity.Project;
import com.datascience.ems.exception.ResourceNotFoundException;
import com.datascience.ems.repository.DepartmentRepository;
import com.datascience.ems.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public ProjectDTO createProject(ProjectDTO dto) {
        Project project = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            project.setDepartment(department);
        }

        Project saved = projectRepository.save(project);
        return convertToDTO(saved);
    }


    @Transactional(readOnly = true)
    public Map<String, Object> getProjectsPaged(int page, int size, String sortBy, Long departmentId) {
        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(page, size,
                        org.springframework.data.domain.Sort.by(sortBy));

        org.springframework.data.domain.Page<Project> pageResult;

        if (departmentId != null) {
            pageResult = projectRepository.findByDepartmentId(departmentId, pageable);
        } else {
            pageResult = projectRepository.findAll(pageable);
        }

        List<ProjectDTO> projects = pageResult.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("projects", projects);
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return response;
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByDepartment(Long departmentId) {
        return projectRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return convertToDTO(project);
    }

    @Transactional
    public ProjectDTO updateProject(Long id, ProjectDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            project.setDepartment(department);
        } else {
            project.setDepartment(null);
        }

        Project updated = projectRepository.save(project);
        return convertToDTO(updated);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setDepartmentId(project.getDepartment() != null ? project.getDepartment().getId() : null);
        dto.setDepartmentName(project.getDepartment() != null ? project.getDepartment().getName() : null);
        dto.setEmployeeCount(project.getEmployeeProjects() != null ? project.getEmployeeProjects().size() : 0);    // ← ADDED NULL CHECK
        return dto;
    }
}
