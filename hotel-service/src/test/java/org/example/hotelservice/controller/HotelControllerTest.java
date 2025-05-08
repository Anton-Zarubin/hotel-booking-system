package org.example.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelservice.dto.hotel.HotelFilter;
import org.example.hotelservice.dto.hotel.HotelListResponse;
import org.example.hotelservice.dto.hotel.HotelResponse;
import org.example.hotelservice.dto.hotel.UpsertHotelRequest;
import org.example.hotelservice.service.HotelService;
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

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(value = HotelController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelService hotelService;

    private UpsertHotelRequest request;

    private HotelResponse hotelResponse;

    private HotelListResponse hotelListResponse;

    @BeforeEach
    public void setUp() {
        request = new UpsertHotelRequest(
                "Arbat Hotel",
                "Moscow",
                "Plotnikov Lane, 12",
                2100
        );

        hotelResponse = new HotelResponse(
                1L,
                "Arbat Hotel",
                "Moscow",
                "Plotnikov Lane, 12",
                2100,
                0.0,
                0
        );

        hotelListResponse = new HotelListResponse(1L, Collections.singletonList(hotelResponse));
    }

    @Test
    public void getAllHotels() throws Exception {
        when(hotelService.getAll(new HotelFilter(), PageRequest.of(0, 1))).thenReturn(hotelListResponse);
        mockMvc.perform(
                        get("/hotels/view")
                                .param("page", "0")
                                .param("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(hotelListResponse.hotels().get(0).name())));
    }

    @Test
    public void getHotelById() throws Exception {
        when(hotelService.getById(1L)).thenReturn(hotelResponse);
        mockMvc.perform(get("/hotels/view/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(hotelResponse.name())));
    }

    @Test
    public void createHotel() throws Exception {
        mockMvc.perform(
                        post("/hotels/add")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void updateHotel() throws Exception {
        mockMvc.perform(
                        put("/hotels/update/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void deleteHotelById() throws Exception {
        doNothing().when(hotelService).deleteById(1L);
        mockMvc.perform(delete("/hotels/delete/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateHotelRatingById() throws Exception {
        mockMvc.perform(
                        put("/hotels/rate/1")
                                .param("newMark", "4")
                )
                .andExpect(status().isOk());
    }
}
