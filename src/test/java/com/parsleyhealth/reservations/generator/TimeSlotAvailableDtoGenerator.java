package com.parsleyhealth.reservations.generator;

import com.parsleyhealth.reservations.dto.TimeSlotAvailableDto;

public class TimeSlotAvailableDtoGenerator {

  public static TimeSlotAvailableDto generateTrueTimeSlotAvailableDto() {

    return TimeSlotAvailableDto.builder()
        .isAvailable(true)
        .build();
  }

  public static TimeSlotAvailableDto generateFalseTimeSlotAvailableDto() {

    return TimeSlotAvailableDto.builder()
        .isAvailable(false)
        .build();
  }
}
