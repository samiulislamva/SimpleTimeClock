package com.time.clock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeClockRequest {

	private Long userId;

	private ClockType clocktype;

}
