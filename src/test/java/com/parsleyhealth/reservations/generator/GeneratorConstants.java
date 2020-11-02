package com.parsleyhealth.reservations.generator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class GeneratorConstants {

  public static final UUID RESERVATION_ID_1 = UUID.randomUUID();
  public static final UUID RESERVATION_ID_2 = UUID.randomUUID();
  public static final ZonedDateTime CREATED_AT_1 = ZonedDateTime.now(ZoneId.of("UTC"))
      .minusHours(1);
  public static final ZonedDateTime MODIFIED_AT_1 = ZonedDateTime.now(ZoneId.of("UTC"))
      .minusHours(1);
  public static final ZonedDateTime START_TIME_1 = ZonedDateTime.now(ZoneId.of("UTC"))
      .plusHours(1);
  public static final ZonedDateTime END_TIME_1 = ZonedDateTime.now(ZoneId.of("UTC"))
      .plusHours(2);
  public static final Long DURATION_1 = 60l;
  public static final Long BAD_DURATION_1 = -60l;
}
