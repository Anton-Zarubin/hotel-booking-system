package org.example.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelservice.dto.hotel.HotelResponse;
import org.example.hotelservice.dto.room.*;
import org.example.hotelservice.service.RoomService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(value = RoomController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomService roomService;

    private RoomResponse roomResponse;

    private RoomListResponse roomListResponse;

    @BeforeEach
    public void setUp() {
        HotelResponse hotelResponse = new HotelResponse(
                1L,
                "Arbat Hotel",
                "Moscow",
                "Plotnikov Lane, 12",
                2100,
                0.0,
                0
        );

        roomResponse = new RoomResponse(
                1L,
                "Standard",
                "Some description",
                21,
                new BigDecimal("8400.00"),
                2,
                hotelResponse,
                new ArrayList<>()
        );

        roomListResponse = new RoomListResponse(1L, Collections.singletonList(roomResponse));
    }

    @Test
    public void getAllRooms() throws Exception {
        when(roomService.getAll(new RoomFilter(), PageRequest.of(0, 1))).thenReturn(roomListResponse);
        mockMvc.perform(
                        get("/rooms/view")
                                .param("page", "0")
                                .param("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(roomListResponse.rooms().get(0).name())));
    }

    @Test
    public void getRoomById() throws Exception {
        when(roomService.getById(1L)).thenReturn(roomResponse);
        mockMvc.perform(get("/rooms/view/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(roomResponse.name())));
    }

    @Test
    public void createRoom() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest(
                "Standard",
                "Some description",
                21,
                new BigDecimal("8400.00"),
                2,
                1L
        );

        mockMvc.perform(
                        post("/rooms/add")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void updateRoom() throws Exception {
        UpdateRoomRequest request = new UpdateRoomRequest(
                "Standard",
                "Some description",
                21,
                new BigDecimal("8400.00"),
                2
        );

        mockMvc.perform(
                        put("/rooms/update/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRoomById() throws Exception {
        doNothing().when(roomService).deleteById(1L);
        mockMvc.perform(delete("/rooms/delete/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
