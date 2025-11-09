package com.datascience.ems.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProjectDTO {
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private String projectName;

    @NotBlank(message = "Role is required")
    @Size(min = 2, max = 50)
    private String role;
}
