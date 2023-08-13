package com.time.clock.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.time.clock.dto.ClockType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table
public class EmployeeTimeClockLog {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employeeId", referencedColumnName = "id")
	private Employee employee;

	@Enumerated(EnumType.STRING)
	private ClockType clockType;

	@NotNull
	@Column
	private Date shiftStartTime;

	@Column
	private Date shiftEndTime;

	@Column
	private Date breakStartTime;

	@Column
	private Date breakEndTime;

	@Column
	private Date lunchStartTime;

	@Column
	private Date lunchEndTime;

}
