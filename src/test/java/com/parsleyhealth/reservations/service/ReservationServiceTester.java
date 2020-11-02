package com.parsleyhealth.reservations.service;

import static com.parsleyhealth.reservations.generator.GeneratorConstants.RESERVATION_ID_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.parsleyhealth.reservations.database.ReservationRepository;
import com.parsleyhealth.reservations.database.model.Reservation;
import com.parsleyhealth.reservations.dto.ReservationDto;
import com.parsleyhealth.reservations.dto.TimeSlotAvailableDto;
import com.parsleyhealth.reservations.dto.TimeSlotDto;
import com.parsleyhealth.reservations.generator.ReservationDtoGenerator;
import com.parsleyhealth.reservations.generator.ReservationGenerator;
import com.parsleyhealth.reservations.generator.TimeSlotDtoGenerator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTester {

  @Mock
  ReservationRepository reservationRepository;
  @Mock
  ConversionService conversionService;

  ReservationService service;

  @Before
  public void setup() {
    service = new ReservationService(reservationRepository, conversionService);
  }

  @Test
  public void findAll_whenNoReservations_returnsEmptySet() {
    when(reservationRepository.findAll()).thenReturn(List.of());

    Set<ReservationDto> results = service.findAll();

    assertEquals(results.size(), 0);
  }

  @Test
  public void findAll_whenReservations_returnsConvertedReservations() {
    ReservationDto reservation1Dto = ReservationDtoGenerator.generateGoodReservation1Dto();
    ReservationDto reservation2Dto = ReservationDtoGenerator.generateGoodReservation2Dto();
    when(conversionService.convert(any(Reservation.class), eq(ReservationDto.class)))
        .thenReturn(reservation1Dto, reservation2Dto);
    Reservation reservation1 = ReservationGenerator.generateGoodReservation1();
    Reservation reservation2 = ReservationGenerator.generateGoodReservation2();
    when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

    Set<ReservationDto> results = service.findAll();

    assertEquals(results.size(), 2);
    results.forEach((result) -> {
      if (result.getId().equals(RESERVATION_ID_1)) {
        assertEquals(result.getId(), reservation1Dto.getId());
        assertEquals(result.getCreatedAt(), reservation1Dto.getCreatedAt());
        assertEquals(result.getModifiedAt(), reservation1Dto.getModifiedAt());
        assertEquals(result.getTimeSlot(), reservation1Dto.getTimeSlot());
      } else {
        assertEquals(result.getId(), reservation2Dto.getId());
        assertEquals(result.getCreatedAt(), reservation2Dto.getCreatedAt());
        assertEquals(result.getModifiedAt(), reservation2Dto.getModifiedAt());
        assertEquals(result.getTimeSlot(), reservation2Dto.getTimeSlot());
      }
    });
  }

  @Test(expected = NullPointerException.class)
  public void isAvailable_inputIsNull_throwsException() {
    service.isAvailable(null);
  }

  @Test
  public void isAvailable_whenNoOverlaps_returnTrue() {
    when(reservationRepository.findOverlapping(any(), any())).thenReturn(List.of());

    TimeSlotAvailableDto result = service
        .isAvailable(TimeSlotDtoGenerator.generateGoodTimeSlotDto());

    assertTrue(result.isAvailable());
  }

  @Test
  public void isAvailable_whenOverlaps_returnFalse() {
    Reservation reservation1 = ReservationGenerator.generateGoodReservation1();
    Reservation reservation2 = ReservationGenerator.generateGoodReservation2();
    when(reservationRepository.findOverlapping(any(), any()))
        .thenReturn(List.of(reservation1, reservation2));

    TimeSlotAvailableDto result = service
        .isAvailable(TimeSlotDtoGenerator.generateGoodTimeSlotDto());

    assertFalse(result.isAvailable());
  }

  @Test(expected = NullPointerException.class)
  public void create_inputIsNull_throwsException() {
    service.create(null);
  }

  @Test(expected = ResponseStatusException.class)
  public void create_whenOverlaps_throwsException() {
    Reservation reservation1 = ReservationGenerator.generateGoodReservation1();
    Reservation reservation2 = ReservationGenerator.generateGoodReservation2();
    when(reservationRepository.findOverlapping(any(), any()))
        .thenReturn(List.of(reservation1, reservation2));

    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    try {
      service.create(timeSlotDto);
    } catch (ResponseStatusException ex) {
      assertEquals(ex.getStatus(), HttpStatus.BAD_REQUEST);
      assertTrue(ex.getMessage().contains(String
          .format("Existing reservation(s) conflict with startTime [%s] and duration [%s]",
              timeSlotDto.getStartTime(), timeSlotDto.getDuration())));
      throw ex;
    }
  }

  @Test(expected = RuntimeException.class)
  public void create_whenNoOverlapsButSaveFails_throwsException() {
    when(reservationRepository.findOverlapping(any(), any()))
        .thenReturn(List.of());
    Reservation reservation = ReservationGenerator.generateGoodReservation1();
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    when(conversionService.convert(eq(timeSlotDto), eq(Reservation.class)))
        .thenReturn(reservation);
    when(reservationRepository.save(any())).thenThrow(new RuntimeException("Problem Saving"));

    try {
      service.create(timeSlotDto);
    } catch (RuntimeException ex) {
      assertTrue(ex.getMessage()
          .contains(String.format("Problem creating reservation [%s]", reservation)));
      throw ex;
    }
  }

  @Test
  public void create_whenNoOverlaps_returnConvertedSavedReservation() {
    when(reservationRepository.findOverlapping(any(), any()))
        .thenReturn(List.of());
    Reservation reservation = ReservationGenerator.generateGoodReservation1();
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    when(conversionService.convert(eq(timeSlotDto), eq(Reservation.class)))
        .thenReturn(reservation);
    when(reservationRepository.save(any())).thenReturn(reservation);
    ReservationDto reservation1Dto = ReservationDtoGenerator.generateGoodReservation1Dto();
    when(conversionService.convert(eq(reservation), eq(ReservationDto.class)))
        .thenReturn(reservation1Dto);

    ReservationDto result = service.create(timeSlotDto);

    assertEquals(result.getId(), reservation1Dto.getId());
    assertEquals(result.getCreatedAt(), reservation1Dto.getCreatedAt());
    assertEquals(result.getModifiedAt(), reservation1Dto.getModifiedAt());
    assertEquals(result.getTimeSlot(), reservation1Dto.getTimeSlot());
  }

  @Test(expected = NullPointerException.class)
  public void delete_inputIsNull_throwsException() {
    service.delete(null);
  }

  @Test(expected = ResponseStatusException.class)
  public void delete_whenNoMatch_throwsException() {
    Optional<Reservation> optional = Optional.empty();
    when(reservationRepository.findById(RESERVATION_ID_1))
        .thenReturn(optional);

    try {
      service.delete(RESERVATION_ID_1);
    } catch (ResponseStatusException ex) {
      assertEquals(ex.getStatus(), HttpStatus.NOT_FOUND);
      assertTrue(ex.getMessage()
          .contains(String.format("Reservation with id [%s] not found", RESERVATION_ID_1)));
      throw ex;
    }
  }

  @Test(expected = RuntimeException.class)
  public void delete_whenMatchButDeleteFails_throwsException() {
    Reservation reservation = ReservationGenerator.generateGoodReservation1();
    Optional<Reservation> optional = Optional.of(reservation);
    when(reservationRepository.findById(RESERVATION_ID_1))
        .thenReturn(optional);
    doThrow(new RuntimeException("Delete Failed")).when(reservationRepository)
        .deleteById(RESERVATION_ID_1);

    try {
      service.delete(RESERVATION_ID_1);
    } catch (RuntimeException ex) {
      assertTrue(ex.getMessage()
          .contains(String.format("Problem deleting reservation [%s]", reservation)));
      throw ex;
    }
  }

  @Test
  public void delete_whenMatch_returnsDeletedDto() {
    Reservation reservation = ReservationGenerator.generateGoodReservation1();
    Optional<Reservation> optional = Optional.of(reservation);
    when(reservationRepository.findById(RESERVATION_ID_1))
        .thenReturn(optional);
    ReservationDto reservation1Dto = ReservationDtoGenerator.generateGoodReservation1Dto();
    when(conversionService.convert(eq(reservation), eq(ReservationDto.class)))
        .thenReturn(reservation1Dto);

    ReservationDto result = service.delete(RESERVATION_ID_1);

    assertEquals(result.getId(), reservation1Dto.getId());
    assertEquals(result.getCreatedAt(), reservation1Dto.getCreatedAt());
    assertEquals(result.getModifiedAt(), reservation1Dto.getModifiedAt());
    assertEquals(result.getTimeSlot(), reservation1Dto.getTimeSlot());
  }
}
