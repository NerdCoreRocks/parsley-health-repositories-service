package com.parsleyhealth.reservations.controller;

import com.parsleyhealth.reservations.dto.ReservationDto;
import com.parsleyhealth.reservations.dto.TimeSlotDto;
import com.parsleyhealth.reservations.service.ReservationService;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// TODO: Strip seconds from times OR throw exception when seconds used in times for DTOs
// TODO: Add user friendly exception handling for errors occurring outside of mapping methods
// TODO: Reformat to include concept of users which contain reservations
// TODO: Add JWT Authorization and endpoint to retrieve Bearer Token for users
// TODO: Add paging
// TODO: Add new endpoints for retrieving overlapping reservations (to make it easier to delete)
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v0/reservations")
public class ReservationController {
  @NonNull
  private final ReservationService reservationService;

  @GetMapping()
  @ResponseBody
  public Set<ReservationDto> getReservations() {
    return reservationService.findAll();
  }

  @PostMapping(path = "/is-available")
  @ResponseBody
  public boolean isAvailable(@NonNull @Valid @RequestBody TimeSlotDto timeSlotDto) {
    return reservationService.isAvailable(timeSlotDto);
  }

  @PostMapping()
  @ResponseBody
  public ReservationDto createReservation(@NonNull @Valid @RequestBody TimeSlotDto timeSlotDto) {
    return reservationService.create(timeSlotDto);
  }

  @DeleteMapping(path = "/{reservationId}")
  @ResponseBody
  public ReservationDto deleteReservation(@NonNull @PathVariable UUID reservationId) {
    return reservationService.delete(reservationId);
  }
}
