package com.time.clock.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.time.clock.dao.EmplyeeRepository;
import com.time.clock.dao.TimeClockRepository;
import com.time.clock.dto.ClockType;
import com.time.clock.dto.TimeClockRequest;
import com.time.clock.exception.TimeClockException;
import com.time.clock.model.Employee;
import com.time.clock.model.EmployeeTimeClockLog;
import com.time.clock.service.TimeClockService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class TimeClockServiceImpl implements TimeClockService {

	private static final String USER_NOT_EXISTS_IN_DB = "User is not available in db with id :";

	private static final String SHIFT_CAN_NOT_BE_STARTED = "Shift can not be started as there is already active shift :";

	private static final String SHIFT_CAN_NOT_BE_END = "Shift can not be ended as there is already active break or lunch :";

	private static final String NO_ACTIVE_SHIFT = "There is no active shift for the user :";
	
	private static final String BREAK_CAN_NOT_BE_STARTED = "Break can not be started as there is already active  break or lunch :";
	
	private static final String BREAK_CAN_NOT_BE_END = "Break can not be ended as there is no active break for the user :";
	
	private static final String LUNCH_CAN_NOT_BE_STARTED = "Lunch break can not be started as there is already active break or lunch :";
	
	private static final String LUNCH_CAN_NOT_BE_END = "Lunch break can not be ended as there is no active lunch break for the user :";
	
	private static final String BREAK_ALREADY_TAKEN = "Break can not be started as this user has already taken a break during this shift :";
	
	private static final String LUNCH_ALREADY_TAKEN = "Lunch break can not be started as this user has already taken a lunch break during this shift :";
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private final EmplyeeRepository employeeRepository;

	private final TimeClockRepository clockRepository;

	@Override
	public EmployeeTimeClockLog startWorkShift(TimeClockRequest timeClockRequest) throws TimeClockException {
		log.info("clock log for employee id : " + timeClockRequest.getUserId() + " clock type : "
				+ timeClockRequest.getClocktype());

		// Validate User id if User is not valid throw Not Found
		Optional<Employee> employee = employeeRepository.findById(timeClockRequest.getUserId());
		if (!employee.isPresent()) {
			throw new TimeClockException(HttpStatus.NOT_FOUND, USER_NOT_EXISTS_IN_DB + timeClockRequest.getUserId());
		}
		
		Date date = new Date(System.currentTimeMillis()); 
		
		EmployeeTimeClockLog timeClockLog = clockRepository.findFirstByEmployeeOrderByIdDesc(employee.get());
		
		// Validate if there is no active shift then throw error
		if (ClockType.SHIFT_START != timeClockRequest.getClocktype() && Objects.isNull(timeClockLog)
				&& !isTodayLog(timeClockLog, date)) {
			throw new TimeClockException(HttpStatus.NOT_FOUND, NO_ACTIVE_SHIFT + timeClockRequest.getUserId());
		}
		
		// validate if shift has ended then throw error
		if(ClockType.SHIFT_START != timeClockRequest.getClocktype() && Objects.nonNull(timeClockLog.getShiftEndTime())) {
			throw new TimeClockException(HttpStatus.NOT_FOUND, NO_ACTIVE_SHIFT + timeClockRequest.getUserId());
		}

		switch (timeClockRequest.getClocktype()) {
		case SHIFT_START:

			// Validate if shift is already started throw error
			if (Objects.nonNull(timeClockLog) && isTodayLog(timeClockLog, date)) {
				throw new TimeClockException(HttpStatus.CONFLICT,
						SHIFT_CAN_NOT_BE_STARTED + timeClockRequest.getUserId());
			}
			timeClockLog = new EmployeeTimeClockLog();
			timeClockLog.setEmployee(employee.get());
			timeClockLog.setShiftStartTime(date);
			break;
		case SHIFT_END:

			// validate if lunch break or break is going on then throw error
			if (timeClockLog.getClockType() == ClockType.BREAK_START
					|| timeClockLog.getClockType() == ClockType.LUNCH_BREAK_START) {
				throw new TimeClockException(HttpStatus.BAD_REQUEST,
						SHIFT_CAN_NOT_BE_END + timeClockRequest.getUserId());
			}
			
			// validate if shift has ended then throw error
			if(Objects.nonNull(timeClockLog.getShiftEndTime())) {
				throw new TimeClockException(HttpStatus.NOT_FOUND, NO_ACTIVE_SHIFT + timeClockRequest.getUserId());
			}
			timeClockLog.setShiftEndTime(date);
			break;
		case BREAK_START:
			
			// validate if lunch break or break is going on then throw error
			if (timeClockLog.getClockType() == ClockType.BREAK_START
					|| timeClockLog.getClockType() == ClockType.LUNCH_BREAK_START) {
				throw new TimeClockException(HttpStatus.CONFLICT,
						BREAK_CAN_NOT_BE_STARTED + timeClockRequest.getUserId());
			}
			
			// validate if a break has already been taken then throw error
			if(Objects.nonNull(timeClockLog.getBreakEndTime())) {
				throw new TimeClockException(HttpStatus.CONFLICT, 
						BREAK_ALREADY_TAKEN + timeClockRequest.getUserId());
			}

			// validate if shift has ended then throw error
			if(Objects.nonNull(timeClockLog.getShiftEndTime())) {
				throw new TimeClockException(HttpStatus.NOT_FOUND, NO_ACTIVE_SHIFT + timeClockRequest.getUserId());
			}
			timeClockLog.setBreakStartTime(date);
			break;
		case BREAK_END:
			
			// validate if there is no active break then throw error
			if (timeClockLog.getClockType() != ClockType.BREAK_START) {
				throw new TimeClockException(HttpStatus.BAD_REQUEST,
						BREAK_CAN_NOT_BE_END + timeClockRequest.getUserId());
			}
			timeClockLog.setBreakEndTime(date);
			break;
		case LUNCH_BREAK_START:
			
			// validate if lunch break or break is going on then throw error
			if (timeClockLog.getClockType() == ClockType.BREAK_START
					|| timeClockLog.getClockType() == ClockType.LUNCH_BREAK_START) {
				throw new TimeClockException(HttpStatus.CONFLICT,
						LUNCH_CAN_NOT_BE_STARTED + timeClockRequest.getUserId());
			}
			
			// validate if a lunch break has already been taken then throw error
			if(Objects.nonNull(timeClockLog.getLunchEndTime())) {
				throw new TimeClockException(HttpStatus.CONFLICT, 
						LUNCH_ALREADY_TAKEN + timeClockRequest.getUserId());
			}

			// validate if shift has ended then throw error
			if(Objects.nonNull(timeClockLog.getShiftEndTime())) {
				throw new TimeClockException(HttpStatus.NOT_FOUND, NO_ACTIVE_SHIFT + timeClockRequest.getUserId());
			}
			timeClockLog.setLunchStartTime(date);
			break;
		case LUNCH_BREAK_END:

			// validate if there is no active lunch break then throw error
			if (timeClockLog.getClockType() != ClockType.LUNCH_BREAK_START) {
				throw new TimeClockException(HttpStatus.BAD_REQUEST,
						LUNCH_CAN_NOT_BE_END + timeClockRequest.getUserId());
			}
			timeClockLog.setLunchEndTime(date);
			break;

		}
		timeClockLog.setClockType(timeClockRequest.getClocktype());
		return clockRepository.save(timeClockLog);
	}

	private boolean isTodayLog(EmployeeTimeClockLog timeClockLog, Date todayDate) {
		
		if(timeClockLog==null) {
			return false;
		}
		if (DateFormatUtils.format(timeClockLog.getShiftStartTime(), DATE_FORMAT)
				.equals(DateFormatUtils.format(todayDate, DATE_FORMAT))) {
			return true;
		}
		return false;
	}

	@Override
	public List<EmployeeTimeClockLog> getAllEmployeeClockLog(Long employeeId) throws TimeClockException {
		// Validate User id if User is not valid throw Not Found
		if(Objects.nonNull(employeeId)) {
			Optional<Employee> employee = employeeRepository.findById(employeeId);
			if (!employee.isPresent()) {
				throw new TimeClockException(HttpStatus.NOT_FOUND, USER_NOT_EXISTS_IN_DB + employeeId);
			}
		}
				
		return clockRepository.findAll(employeeId);
	}

}
