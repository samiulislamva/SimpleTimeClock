package com.time.clock.service;

import java.util.List;

import com.time.clock.dto.TimeClockRequest;
import com.time.clock.exception.TimeClockException;
import com.time.clock.model.EmployeeTimeClockLog;

public interface TimeClockService {

	EmployeeTimeClockLog startWorkShift(TimeClockRequest timeClockRequest) throws TimeClockException;

	List<EmployeeTimeClockLog> getAllEmployeeClockLog(Long employeeId) throws TimeClockException;

}
