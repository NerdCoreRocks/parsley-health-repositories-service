package com.parsleyhealth.reservations.service;

import com.parsleyhealth.reservations.database.ReservationRepository;
import com.parsleyhealth.reservations.database.model.Reservation;
import com.parsleyhealth.reservations.dto.ReservationDto;
import com.parsleyhealth.reservations.dto.TimeSlotAvailableDto;
import com.parsleyhealth.reservations.dto.TimeSlotDto;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReservationService {

  @NonNull ReservationRepository reservationRepository;
  @NonNull ConversionService conversionService;

  public Set<ReservationDto> findAll(
  ) {
    Iterable<Reservation> reservations = reservationRepository.findAll();

    return StreamSupport.stream(reservations.spliterator(), false)
        .map(reservation -> conversionService.convert(reservation, ReservationDto.class))
        .collect(Collectors.toSet());
  }

  public TimeSlotAvailableDto isAvailable(@NonNull TimeSlotDto timeSlotDto) {
    boolean isAvailable = !overlappingReservationExists(timeSlotDto);
    return TimeSlotAvailableDto.builder()
        .isAvailable(isAvailable)
        .build();
  }

  public ReservationDto create(@NonNull TimeSlotDto timeSlotDto) {
    if (overlappingReservationExists(timeSlotDto)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String
          .format("Existing reservation(s) conflict with startTime [%s] and duration [%s]",
              timeSlotDto.getStartTime(), timeSlotDto.getDuration()));
    }

    Reservation newReservation = conversionService.convert(timeSlotDto, Reservation.class);
    Reservation savedReservation;
    try {
      savedReservation = reservationRepository.save(newReservation);
    } catch (RuntimeException ex) {
      throw new RuntimeException(
          String.format("Problem creating reservation [%s]", newReservation));
    }

    return conversionService.convert(savedReservation, ReservationDto.class);
  }

  public ReservationDto delete(@NonNull UUID id) {
    Reservation reservation = findById(id);
    try {
      reservationRepository.deleteById(id);
    } catch (RuntimeException ex) {
      throw new RuntimeException(
          String.format("Problem deleting reservation [%s]", reservation));
    }

    return conversionService.convert(reservation, ReservationDto.class);
  }

  private Reservation findById(@NonNull UUID id) {
    return reservationRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("Reservation with id [%s] not found", id)));
  }

  private boolean overlappingReservationExists(@NonNull TimeSlotDto timeSlotDto) {
    ZonedDateTime startTime = timeSlotDto.getStartTime();
    ZonedDateTime endTime = timeSlotDto.getStartTime()
        .plusMinutes(timeSlotDto.getDuration());
    List<Reservation> reservations = reservationRepository.findOverlapping(startTime, endTime);

    return !reservations.isEmpty();
  }
}
