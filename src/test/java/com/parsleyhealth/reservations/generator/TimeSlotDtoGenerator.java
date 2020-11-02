package com.parsleyhealth.reservations.generator;

import static com.parsleyhealth.reservations.generator.GeneratorConstants.BAD_DURATION_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.DURATION_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.START_TIME_1;

import com.parsleyhealth.reservations.dto.TimeSlotDto;

public class TimeSlotDtoGenerator {
  public static TimeSlotDto generateGoodTimeSlotDto() {

    return TimeSlotDto.builder()
        .startTime(START_TIME_1)
        .duration(DURATION_1)
        .build();
  }

  public static TimeSlotDto generateBadTimeSlotDto() {

    return TimeSlotDto.builder()
        .startTime(START_TIME_1)
        .duration(BAD_DURATION_1)
        .build();
  }
}
