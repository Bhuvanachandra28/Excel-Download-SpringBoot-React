package com.example.crud.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.crud.Model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query(value = "SELECT * FROM employee e WHERE e.emp_city = ?1", nativeQuery = true)
	public List<Employee> findByEmp_city(String emp_city);

}
