package com.datascience.ems.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {
    private Long id;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 200)
    private String location;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0")
    private BigDecimal budget;

    private Integer employeeCount;
    private Integer projectCount;
}

