package com.parsleyhealth.reservations.database;

import com.parsleyhealth.reservations.database.model.Reservation;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {

  @Query("FROM Reservation WHERE (startTime < ?1 AND endTime > ?1) OR (startTime >= ?1 AND startTime < ?2)")
  List<Reservation> findOverlapping(ZonedDateTime startTime, ZonedDateTime endTime);
}
