package com.parsleyhealth.reservations.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.parsleyhealth.reservations.dto.TimeSlotDto.TimeSlotDtoBuilder;
import java.time.ZonedDateTime;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@JsonDeserialize(builder = TimeSlotDtoBuilder.class)
@Builder
@Value
public class TimeSlotDto {
  @NonNull ZonedDateTime startTime;
  @NonNull @Positive
  Long duration;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TimeSlotDtoBuilder {

  }
}
