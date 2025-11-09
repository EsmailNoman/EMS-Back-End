package com.datascience.ems.repository;

import com.datascience.ems.entity.Employee;
import com.datascience.ems.entity.EmployeeProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long> {
    List<EmployeeProject> findByEmployeeId(Long employeeId);
    List<EmployeeProject> findByProjectId(Long projectId);
    Optional<EmployeeProject> findByEmployeeIdAndProjectId(Long employeeId, Long projectId);

}