package com.example.springrestapi.interestpoints.controller;

import com.example.springrestapi.interestpoints.model.Place;
import com.example.springrestapi.interestpoints.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlaceController.class)
public class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceRepository placeRepository;

    @Test
    @WithMockUser
    public void shouldReturnAllPlaces() throws Exception {
        Place place1 = new Place();
        place1.setId(1L);
        place1.setName("Place 1");
        place1.setLocation("Location 1");
        place1.setDescription("Description 1");

        Place place2 = new Place();
        place2.setId(2L);
        place2.setName("Place 2");
        place2.setLocation("Location 2");
        place2.setDescription("Description 2");

        List<Place> allPlaces = Arrays.asList(place1, place2);

        given(placeRepository.findByIsPublicTrueAndIsDeletedFalse()).willReturn(allPlaces);

        mockMvc.perform(get("/api/places")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'name':'Place 1','location':'Location 1','description':'Description 1'},{'id':2,'name':'Place 2','location':'Location 2','description':'Description 2'}]"));
    }
}