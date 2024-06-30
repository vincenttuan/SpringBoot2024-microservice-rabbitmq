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
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

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
	
	@RateLimiter(name = "employeeRateLimiter", fallbackMethod = "getCompletableFutureEmployeeFallback")
	@GetMapping("/ratelimit/{empId}")
	public CompletableFuture<Employee> getEmployeeRateLimiter(@PathVariable Integer empId) {
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
	
	@TimeLimiter(name = "employeeTimeLimiter", fallbackMethod = "getEmployeeFallback")
	@GetMapping("/timelimiter/{empId}")
	public Employee getEmployeeTimeLimiter(@PathVariable Integer empId) throws InterruptedException {
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
	
	// 這是一個回退方法(Fallback)，當 getEmployeeThreadPool 方法發生異常時，將調用此方法
	// Bulkhead.Type.THREADPOOL 使用線程池來限制同時執行的線程數量，以防止系統過載。
	// 使用線程池時，我們需要確保方法的執行和異常處理都是在線程池中進行，而不是在主線程中進行，這樣才能有效地隔離和保護主線程。
	// 所以當使用 Bulkhead.Type.THREADPOOL 時，方法的返回值必須是 CompletableFuture，這是一種異步計算的結果。
    public CompletableFuture<Employee> getCompletableFutureEmployeeFallback(Integer empId, Throwable t) {
		return CompletableFuture.supplyAsync(() -> {
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
		});
	}
}
