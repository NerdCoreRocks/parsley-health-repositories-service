package com.parsleyhealth.reservations.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.parsleyhealth.reservations.dto.TimeSlotAvailableDto.TimeSlotAvailableDtoBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@JsonDeserialize(builder = TimeSlotAvailableDtoBuilder.class)
@Builder
@Value
public class TimeSlotAvailableDto {

  @NonNull
  boolean isAvailable;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TimeSlotAvailableDtoBuilder {

  }
}
