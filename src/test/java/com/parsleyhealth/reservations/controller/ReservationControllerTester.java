package com.parsleyhealth.reservations.controller;

import static com.parsleyhealth.reservations.generator.GeneratorConstants.RESERVATION_ID_1;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.parsleyhealth.reservations.database.model.Reservation;
import com.parsleyhealth.reservations.dto.ReservationDto;
import com.parsleyhealth.reservations.dto.TimeSlotAvailableDto;
import com.parsleyhealth.reservations.dto.TimeSlotDto;
import com.parsleyhealth.reservations.generator.ReservationDtoGenerator;
import com.parsleyhealth.reservations.generator.ReservationGenerator;
import com.parsleyhealth.reservations.generator.TimeSlotAvailableDtoGenerator;
import com.parsleyhealth.reservations.generator.TimeSlotDtoGenerator;
import com.parsleyhealth.reservations.service.ReservationService;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReservationController.class)
public class ReservationControllerTester {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  ReservationService reservationService;

  @Test
  public void getReservations_whenServiceReturnsEmptySet_returnsJson() throws Exception {
    when(reservationService.findAll()).thenReturn(Set.of());
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    JSONAssert.assertEquals(result.getResponse().getContentAsString(), "[]", false);
  }

  @Test
  public void getReservations_whenServiceReturnsSet_returnsJson() throws Exception {
    ReservationDto reservation1Dto = ReservationDtoGenerator.generateGoodReservation1Dto();
    ReservationDto reservation2Dto = ReservationDtoGenerator.generateGoodReservation2Dto();
    Set<ReservationDto> reservationDtos = Set.of(reservation1Dto, reservation2Dto);
    when(reservationService.findAll()).thenReturn(reservationDtos);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    JSONAssert.assertEquals(result.getResponse().getContentAsString(),
        objectMapper.writeValueAsString(reservationDtos), false);
  }

  @Test
  public void isReservationAvailable_whenBodyIsEmpty_returns400() throws Exception {
    String jsonBody = "";
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations/is-available")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void isReservationAvailable_whenBodyIsPartial_returns400() throws Exception {
    String jsonBody = "{ \"startTime\": \"2020-11-20T11:00:01Z\" }";
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations/is-available")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void isReservationAvailable_whenBadDuration_returns400() throws Exception {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateBadTimeSlotDto();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String jsonBody = objectMapper.writeValueAsString(timeSlotDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations/is-available")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void isReservationAvailable_serviceReturnsTrue_returnsTrue() throws Exception {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    TimeSlotAvailableDto timeSlotAvailableDto = TimeSlotAvailableDtoGenerator.generateTrueTimeSlotAvailableDto();
    when(reservationService.isAvailable(eq(timeSlotDto))).thenReturn(timeSlotAvailableDto);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String jsonBody = objectMapper.writeValueAsString(timeSlotDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations/is-available")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    JSONAssert.assertEquals(result.getResponse().getContentAsString(),
        objectMapper.writeValueAsString(timeSlotAvailableDto), false);
  }

  @Test
  public void isReservationAvailable_serviceReturnsFalse_returnsFalse() throws Exception {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    TimeSlotAvailableDto timeSlotAvailableDto = TimeSlotAvailableDtoGenerator.generateFalseTimeSlotAvailableDto();
    when(reservationService.isAvailable(eq(timeSlotDto))).thenReturn(timeSlotAvailableDto);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String jsonBody = objectMapper.writeValueAsString(timeSlotDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations/is-available")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    JSONAssert.assertEquals(result.getResponse().getContentAsString(),
        objectMapper.writeValueAsString(timeSlotAvailableDto), false);
  }

  @Test
  public void createReservation_whenBodyIsEmpty_returns400() throws Exception {
    String jsonBody = "";
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void createReservation_whenBodyIsPartial_returns400() throws Exception {
    String jsonBody = "{ \"startTime\": \"2020-11-20T11:00:01Z\" }";
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void createReservation_whenBadDuration_returns400() throws Exception {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateBadTimeSlotDto();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String jsonBody = objectMapper.writeValueAsString(timeSlotDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void createReservation_serviceCreateThrowsNotFound_returns404() throws Exception {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, String
        .format("Existing reservation(s) conflict with startTime [%s] and duration [%s]",
            timeSlotDto.getStartTime(), timeSlotDto.getDuration()));
    when(reservationService.create(eq(timeSlotDto))).thenThrow(exception);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String jsonBody = objectMapper.writeValueAsString(timeSlotDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test(expected = NestedServletException.class)
  public void createReservation_serviceCreateThrowsRunTime_throwsException() throws Exception {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    Reservation newReservation = ReservationGenerator.generateGoodReservation1();
    RuntimeException exception = new RuntimeException(
        String.format("Problem creating reservation [%s]", newReservation));
    when(reservationService.create(eq(timeSlotDto))).thenThrow(exception);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String jsonBody = objectMapper.writeValueAsString(timeSlotDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestBuilder).andReturn();
  }

  @Test
  public void createReservation_serviceCreateIsGood_returnsDeletedDto() throws Exception {
    TimeSlotDto timeSlotDto = TimeSlotDtoGenerator.generateGoodTimeSlotDto();
    ReservationDto reservationDto = ReservationDtoGenerator.generateGoodReservation1Dto();
    when(reservationService.create(eq(timeSlotDto))).thenReturn(reservationDto);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String jsonBody = objectMapper.writeValueAsString(timeSlotDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/v0/reservations")
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    JSONAssert.assertEquals(result.getResponse().getContentAsString(),
        objectMapper.writeValueAsString(reservationDto), false);
  }

  @Test
  public void deleteReservation_whenIdIsEmpty_returns400() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/api/v0/reservations/")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.METHOD_NOT_ALLOWED.value());
  }

  @Test
  public void deleteReservation_whenIdIsBad_returns400() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/api/v0/reservations/1111-1111")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void deleteReservation_serviceDeleteReturnsNotFound_returns404() throws Exception {
    ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND,
        String.format("Reservation with id [%s] not found", RESERVATION_ID_1));
    when(reservationService.delete(eq(RESERVATION_ID_1))).thenThrow(exception);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/api/v0/reservations/" + RESERVATION_ID_1)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
  }

  @Test(expected = NestedServletException.class)
  public void deleteReservation_serviceDeleteThrowsRunTime_throwsException() throws Exception {
    Reservation reservation = ReservationGenerator.generateGoodReservation1();
    RuntimeException exception = new RuntimeException(
        String.format("Problem deleting reservation [%s]", reservation));
    when(reservationService.delete(eq(RESERVATION_ID_1))).thenThrow(exception);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/api/v0/reservations/" + RESERVATION_ID_1)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestBuilder).andReturn();
  }

  @Test
  public void deleteReservation_serviceDeleteIsGood_returnsDeletedDto() throws Exception {
    ReservationDto reservationDto = ReservationDtoGenerator.generateGoodReservation1Dto();
    when(reservationService.delete(eq(RESERVATION_ID_1))).thenReturn(reservationDto);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/api/v0/reservations/" + RESERVATION_ID_1)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    JSONAssert.assertEquals(result.getResponse().getContentAsString(),
        objectMapper.writeValueAsString(reservationDto), false);
  }


}
