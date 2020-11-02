package com.parsleyhealth.reservations.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.parsleyhealth.reservations.database.model.Reservation;
import com.parsleyhealth.reservations.dto.TimeSlotDto;
import com.parsleyhealth.reservations.generator.TimeSlotDtoGenerator;
import org.junit.Test;

public class TimeSlotDtoToReservationConverterTester {

  private final TimeSlotDtoToReservationConverter converter;

  public TimeSlotDtoToReservationConverterTester() {
    converter = new TimeSlotDtoToReservationConverter();
  }

  @Test
  public void convertConvertsTimeSlotDtoToReservation() {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();

    Reservation result = converter.convert(timeSlotDto);

    assertNotNull(result.getId());
    assertNotNull(result.getCreatedAt());
    assertNotNull(result.getModifiedAt());
    assertEquals(result.getStartTime(), timeSlotDto.getStartTime());
    assertEquals(result.getEndTime(),
        timeSlotDto.getStartTime().plusMinutes(timeSlotDto.getDuration()));
  }
}
