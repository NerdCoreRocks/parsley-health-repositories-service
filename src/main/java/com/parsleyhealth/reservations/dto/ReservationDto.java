package com.parsleyhealth.reservations.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.parsleyhealth.reservations.dto.ReservationDto.ReservationDtoBuilder;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@JsonDeserialize(builder = ReservationDtoBuilder.class)
@Builder
@Value
public class ReservationDto {

  @NonNull UUID id;
  @NonNull TimeSlotDto timeSlot;
  @NonNull ZonedDateTime createdAt;
  @NonNull ZonedDateTime modifiedAt;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ReservationDtoBuilder {

  }
}
