package com.parsleyhealth.reservations.database.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
  @Id
  @Builder.Default
  @Column(columnDefinition = "BINARY(16)")
  @NonNull
  private final UUID id = UUID.randomUUID();
  @NonNull
  private ZonedDateTime startTime;
  @NonNull
  private ZonedDateTime endTime;

  @Builder.Default
  @NonNull
  private final ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
  @Builder.Default
  @NonNull
  private final ZonedDateTime modifiedAt = ZonedDateTime.now(ZoneId.of("UTC"));
}
