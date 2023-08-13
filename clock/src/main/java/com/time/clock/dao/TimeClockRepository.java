package com.time.clock.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.time.clock.model.Employee;
import com.time.clock.model.EmployeeTimeClockLog;

public interface TimeClockRepository extends JpaRepository<EmployeeTimeClockLog, Long> {

	EmployeeTimeClockLog findFirstByEmployeeOrderByIdDesc(Employee employee);

	@Query("SELECT timeClockLog from EmployeeTimeClockLog timeClockLog where (:employeeId is null or timeClockLog.employee.id=:employeeId)")
	List<EmployeeTimeClockLog> findAll(Long employeeId);
}
