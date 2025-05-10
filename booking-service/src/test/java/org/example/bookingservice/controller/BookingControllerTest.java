package org.example.bookingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookingservice.domain.BookingStatus;
import org.example.bookingservice.dto.BookingListResponse;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(value = BookingController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingResponse bookingResponse;

    private BookingListResponse bookingListResponse;

    @BeforeEach
    public void setUp() {
        bookingResponse = new BookingResponse(
                1L,
                1L,
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                BookingStatus.NEW,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        bookingListResponse = new BookingListResponse(1L, Collections.singletonList(bookingResponse));
    }

    @Test
    public void getAllBookings() throws Exception {
        when(bookingService.getAll(1L, false, PageRequest.of(0, 1))).thenReturn(bookingListResponse);
        mockMvc.perform(
                        get("/bookings")
                                .with(request -> {
                                    request.addHeader("id", 1L);
                                    request.addHeader("roles", "ROLE_USER");
                                    return request;
                                })
                                .param("page", "0")
                                .param("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bookingListResponse.bookings().get(0).status().toString())));
    }

    @Test
    public void getBookingById() throws Exception {
        when(bookingService.getById(1L)).thenReturn(bookingResponse);
        mockMvc.perform(get("/bookings/1")
                        .with(request -> {
                            request.addHeader("id", 1L);
                            request.addHeader("roles", "ROLE_USER");
                            return request;
                        })
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bookingResponse.status().toString())));
    }

    @Test
    public void getBookingWithException() throws Exception {
        when(bookingService.getById(1L)).thenReturn(bookingResponse);
        mockMvc.perform(get("/bookings/1")
                        .with(request -> {
                            request.addHeader("id", 2L);
                            request.addHeader("roles", "ROLE_USER");
                            return request;
                        })
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void createBooking() throws Exception {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        mockMvc.perform(
                        post("/bookings/add")
                                .with(request -> {
                                    request.addHeader("id", 1L);
                                    return request;
                                })
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createBookingRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }
}
