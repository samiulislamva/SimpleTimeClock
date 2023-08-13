package com.time.clock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.time.clock.dto.TimeClockRequest;
import com.time.clock.exception.TimeClockException;
import com.time.clock.model.EmployeeTimeClockLog;
import com.time.clock.service.TimeClockService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController()
@Slf4j
@AllArgsConstructor
@RequestMapping("/time-clock/log")
public class TimeClockController {

	private final TimeClockService timeClockService;

	@PostMapping
	public ResponseEntity<EmployeeTimeClockLog> startWorkShift(@RequestBody TimeClockRequest timeClockRequest)
			throws TimeClockException {
		return ResponseEntity.status(HttpStatus.OK.value()).body(timeClockService.startWorkShift(timeClockRequest));
	}

	@GetMapping
	public ResponseEntity<List<EmployeeTimeClockLog>> getAllEmployeeClockLog(
			@RequestParam(name = "employeeId", required = false) Long employeeId) throws TimeClockException {
		return ResponseEntity.status(HttpStatus.OK.value()).body(timeClockService.getAllEmployeeClockLog(employeeId));
	}

}
