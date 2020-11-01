package com.parsleyhealth.reservations.converter;

import com.parsleyhealth.reservations.database.model.Reservation;
import com.parsleyhealth.reservations.dto.TimeSlotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeSlotDtoToReservationConverter implements Converter<TimeSlotDto, Reservation> {

  @Override
  public Reservation convert(TimeSlotDto source) {

    return Reservation.builder()
        .startTime(source.getStartTime())
        .endTime(source.getStartTime().plusMinutes(source.getDuration()))
        .build();
  }
}
