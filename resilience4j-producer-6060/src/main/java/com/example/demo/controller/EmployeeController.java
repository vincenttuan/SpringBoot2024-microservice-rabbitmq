package com.example.demo.controller;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	// 若有錯誤發生會由處理特定的全局異常來處理
	@GetMapping("/{empId}")
	public Employee getEmployee(@PathVariable Integer empId) throws InterruptedException {
		// 若有錯誤發生會由處理特定的全局異常來處理
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
	
	// 若有錯誤發生可以自行處理中斷
	@CircuitBreaker(name = "employeeCircuitBreaker", fallbackMethod = "getEmployeeFallback")
	@GetMapping("/breaker/{empId}")
	public Employee getEmployeeBreaker(@PathVariable Integer empId) throws InterruptedException {
		// 若有錯誤發生會觸發 CircuitBreaker 而不會由全局異常來處理
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
	
	@Retry(name = "employeeRetry", fallbackMethod = "getEmployeeFallback")
	@GetMapping("/retry/{empId}")
	public Employee getEmployeeRetry(@PathVariable Integer empId) throws InterruptedException {
		
		if(empId < 1 ) {
			throw new RuntimeException("無此員編");
		} else if(empId >= 10) {
			throw new RuntimeException("網路負荷過重連線失敗...");
		}
		
		int randonNumber = new Random().nextInt(100);
		System.out.printf("randonNumber: %d%n", randonNumber);
		if(randonNumber < 50) {
			throw new RuntimeException("資料庫存取錯誤...");
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
	
	@Bulkhead(name = "employeeBulkhead", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getEmployeeFallback")
	@GetMapping("/semaphore/{empId}")
	public Employee getEmployeeSemaphore(@PathVariable Integer empId) throws InterruptedException {
		if(empId < 1 ) {
			throw new RuntimeException("無此員編");
		} else if(empId >= 10) {
			throw new RuntimeException("網路負荷過重連線失敗...");
		}
		
		// 模擬業務處理延遲
		// 當 localhost:6060/semaphore/1 同時連續執行多次會印出 "Bulkhead call rejected"
		Thread.sleep(2000);
		
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmpName("John" + empId);
		emp.setDescription("Manager" + empId);
		emp.setSalary(30000.0 * empId);
		
		return emp;
	}
	
	@Bulkhead(name = "employeeThreadPoolBulkhead", type = Bulkhead.Type.THREADPOOL, 
			  fallbackMethod = "getCompletableFutureEmployeeFallback")
	@GetMapping("/threadpool/{empId}")
	public CompletableFuture<Employee> getEmployeeThreadPool(@PathVariable Integer empId) {
		return CompletableFuture.supplyAsync(() -> {
			try {
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
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		
	}
	
	// 回退方法 ------------------------------------------------------------------------
	public Employee getEmployeeFallback(Integer empId, Throwable t) {
		// 此錯誤會由全局異常處理
		if(empId == 0) {
			throw new RuntimeException("無此員編 0");
		}
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmpName("Fallback");
		emp.setDescription(t.getMessage());
		emp.setSalary(null);
		return emp;
	}
}
