package com.parsleyhealth.reservations.converter;

import static org.junit.Assert.assertEquals;

import com.parsleyhealth.reservations.database.model.Reservation;
import com.parsleyhealth.reservations.dto.ReservationDto;
import com.parsleyhealth.reservations.generator.ReservationGenerator;
import java.time.Duration;
import org.junit.Test;

public class ReservationToReservationDtoConverterTester {

  private final ReservationToReservationDtoConverter converter;

  public ReservationToReservationDtoConverterTester() {
    converter = new ReservationToReservationDtoConverter();
  }

  @Test
  public void convertConvertsReservationToReservationDto() {
    Reservation reservation = ReservationGenerator.generateGoodReservation1();

    ReservationDto result = converter.convert(reservation);

    assertEquals(result.getId(), reservation.getId());
    assertEquals(result.getCreatedAt(), reservation.getCreatedAt());
    assertEquals(result.getModifiedAt(), reservation.getModifiedAt());
    assertEquals(result.getTimeSlot().getStartTime(), reservation.getStartTime());
    assertEquals(result.getTimeSlot().getDuration().longValue(),
        Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes());
  }
}
