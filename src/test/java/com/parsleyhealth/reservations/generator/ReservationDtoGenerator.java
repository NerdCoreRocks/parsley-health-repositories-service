package com.parsleyhealth.reservations.generator;

import static com.parsleyhealth.reservations.generator.GeneratorConstants.CREATED_AT_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.MODIFIED_AT_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.RESERVATION_ID_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.RESERVATION_ID_2;

import com.parsleyhealth.reservations.dto.ReservationDto;

public class ReservationDtoGenerator {

  public static ReservationDto generateGoodReservation1Dto() {
    return ReservationDto.builder()
        .id(RESERVATION_ID_1)
        .createdAt(CREATED_AT_1)
        .modifiedAt(MODIFIED_AT_1)
        .timeSlot(TimeSlotDtoGenerator.generateGoodTimeSlotDto())
        .build();
  }

  public static ReservationDto generateGoodReservation2Dto() {
    return ReservationDto.builder()
        .id(RESERVATION_ID_2)
        .createdAt(CREATED_AT_1)
        .modifiedAt(MODIFIED_AT_1)
        .timeSlot(TimeSlotDtoGenerator.generateGoodTimeSlotDto())
        .build();
  }
}
