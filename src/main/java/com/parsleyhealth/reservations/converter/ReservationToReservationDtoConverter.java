package com.parsleyhealth.reservations.converter;

import com.parsleyhealth.reservations.database.model.Reservation;
import com.parsleyhealth.reservations.dto.ReservationDto;
import com.parsleyhealth.reservations.dto.TimeSlotDto;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationToReservationDtoConverter implements
    Converter<Reservation, ReservationDto> {

  @Override
  public ReservationDto convert(Reservation source) {
    long durationMinutes = Duration.between(source.getStartTime(), source.getEndTime()).toMinutes();

    TimeSlotDto timeSlotDto = TimeSlotDto.builder()
        .startTime(source.getStartTime())
        .duration(durationMinutes)
        .build();

    return ReservationDto.builder()
        .id(source.getId())
        .createdAt(source.getCreatedAt())
        .modifiedAt(source.getModifiedAt())
        .timeSlot(timeSlotDto)
        .build();
  }
}
