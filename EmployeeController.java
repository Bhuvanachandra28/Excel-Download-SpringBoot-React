package com.example.crud.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.Model.Employee;
import com.example.crud.Repository.EmployeeRepository;


@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;

	@PostMapping("/employees")
	public String createNewEmployee(@RequestBody Employee employee) {
		employeeRepository.save(employee);
		return "New Employee created";

	}

	// my code
	@GetMapping("/allemployees")
	public List<Employee> getAllEmployees() {
		List<Employee> emp = new ArrayList<Employee>();
		emp = employeeRepository.findAll();
		return emp;
	}

	//
	@GetMapping("/allemployeesnew")
	public ResponseEntity<List<Employee>> findAllEmployees() {
		List<Employee> empList = new ArrayList<Employee>();
		employeeRepository.findAll().forEach(empList::add);
		return new ResponseEntity<List<Employee>>(empList, HttpStatus.OK);
	}

	// optional is used if we retrive data form the database if it not exits then
	// there may be chance of throwing null pointer exception
	// but buy using optional(functional interface) we can avoid those conditions by
	// using if block
	// my code
	@GetMapping("/employees/{empid}")
	public Optional<Employee> findEmployeeById(@PathVariable Long empid) {
		Optional<Employee> emp = employeeRepository.findById(empid);
		return emp;

	}

	@GetMapping("/employeesnew/{empid}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long empid) {
		Optional<Employee> emp = employeeRepository.findById(empid);
		if (emp.isPresent()) {
			return new ResponseEntity<Employee>(emp.get(), HttpStatus.FOUND);
		} else {
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/employeesnew/{empid}")
	public String updateEmployee(@PathVariable Long empid, @RequestBody Employee employee) {
		Optional<Employee> emp = employeeRepository.findById(empid);
		if (emp.isPresent()) {
			Employee existEmp = emp.get();
			existEmp.setEmp_age(employee.getEmp_age());
			existEmp.setEmp_city(employee.getEmp_city());
			existEmp.setEmp_name(employee.getEmp_name());
			existEmp.setEmp_salary(employee.getEmp_salary());
			employeeRepository.save(existEmp);
			return "Employee Details against Id" + empid + "updated";
		}

		return "Employee Details against Id" + empid + "Not found";

	}

	@DeleteMapping("/employeesnew/{empid}")
	public String deleteEmployee(@PathVariable Long empid) {
		Optional<Employee> emp = employeeRepository.findById(empid);
		if (emp.isPresent()) {
			employeeRepository.deleteById(empid);
			return "Employee bearing " + empid + " Deleted succesfully";
		} else {
			return "Employee bearing " + empid + " NOT_FOUND";
		}
	}

//	@GetMapping("/employeesnew/empcity/{emp_city}")
//	public List<Employee> getEmployeeByCity(@PathVariable String emp_city) {
//		List<Employee> emp = employeeRepository.findAll();
//		List<Employee> newemp = new ArrayList<Employee>();
//			for (Employee emp_city1 : emp) {
//				if (emp_city1.getEmp_city().equalsIgnoreCase(emp_city)) {
//					newemp.add(emp_city1);
//				}
//
//			}
//			return newemp;
//	}
	
	@GetMapping("/employeesnew/empcity/{emp_city}")
	public List<Employee> getEmployeeBycity(@PathVariable String emp_city){
		return employeeRepository.findByEmp_city(emp_city);
	}
	  
    @GetMapping("/api/employees")
    public List<Employee> getAllEmployees2() {
        return employeeRepository.findAll();
    }

    @GetMapping(value = "/api/export-employees", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportEmployees(HttpServletResponse response) throws IOException {
        List<Employee> employees = employeeRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Salary");
        headerRow.createCell(3).setCellValue("Age");
        headerRow.createCell(4).setCellValue("City");

        int rowNum = 1;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getEmpid());
            row.createCell(1).setCellValue(employee.getEmp_name());
            row.createCell(2).setCellValue(employee.getEmp_salary());
            row.createCell(3).setCellValue(employee.getEmp_age());
            row.createCell(4).setCellValue(employee.getEmp_city());
        }

        // Set headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

        // Write workbook to response output stream
        workbook.write(response.getOutputStream());
        workbook.close();

        // Return ResponseEntity
        return ResponseEntity.ok().build();
}
}
