package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@GetMapping("/{empId}")
	public Employee getEmployee(@PathVariable Integer empId) throws InterruptedException {
		
		if(empId < 1 ) {
			throw new RuntimeException("無此員編");
		} else if(empId >= 10) {
			throw new RuntimeException("網路負荷過重連線失敗...");
		}
		
		// 模擬業務處理延遲
		Thread.sleep(2000);
		
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmpName("John" + empId);
		emp.setDescription("Manager" + empId);
		emp.setSalary(30000.0 * empId);
		
		return emp;
	}
	
}
