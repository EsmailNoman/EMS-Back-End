package com.datascience.ems.controller;


import com.datascience.ems.dto.EmployeeProjectDTO;
import com.datascience.ems.service.EmployeeProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employee-projects")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeProjectController {

    private final EmployeeProjectService employeeProjectService;
    @Autowired
    public EmployeeProjectController(EmployeeProjectService employeeProjectService) {
        this.employeeProjectService = employeeProjectService;
    }

    @PostMapping
    public ResponseEntity<EmployeeProjectDTO> assignEmployeeToProject(
            @Valid @RequestBody EmployeeProjectDTO dto) {
        return new ResponseEntity<>(
                employeeProjectService.assignEmployeeToProject(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeProjectDTO>> getProjectsByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeProjectService.getProjectsByEmployee(employeeId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<EmployeeProjectDTO>> getEmployeesByProject(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(employeeProjectService.getEmployeesByProject(projectId));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<EmployeeProjectDTO> updateRole(
            @PathVariable Long id, @RequestParam String role) {
        return ResponseEntity.ok(employeeProjectService.updateEmployeeProjectRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeEmployeeFromProject(@PathVariable Long id) {
        employeeProjectService.removeEmployeeFromProject(id);
        return ResponseEntity.noContent().build();
    }
}
