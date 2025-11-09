package com.datascience.ems.repository;

import com.datascience.ems.entity.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);
    Optional<Employee> findByName(String email);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByPhone(String phone);

}
