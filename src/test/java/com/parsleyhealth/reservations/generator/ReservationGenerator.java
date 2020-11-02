package com.parsleyhealth.reservations.generator;

import static com.parsleyhealth.reservations.generator.GeneratorConstants.CREATED_AT_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.END_TIME_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.MODIFIED_AT_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.RESERVATION_ID_1;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.RESERVATION_ID_2;
import static com.parsleyhealth.reservations.generator.GeneratorConstants.START_TIME_1;

import com.parsleyhealth.reservations.database.model.Reservation;

public class ReservationGenerator {

  public static Reservation generateGoodReservation1() {
    return Reservation.builder()
        .id(RESERVATION_ID_1)
        .createdAt(CREATED_AT_1)
        .modifiedAt(MODIFIED_AT_1)
        .startTime(START_TIME_1)
        .endTime(END_TIME_1)
        .build();
  }

  public static Reservation generateGoodReservation2() {
    return Reservation.builder()
        .id(RESERVATION_ID_2)
        .createdAt(CREATED_AT_1)
        .modifiedAt(MODIFIED_AT_1)
        .startTime(START_TIME_1)
        .endTime(END_TIME_1)
        .build();
  }
}
