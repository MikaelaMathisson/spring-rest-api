package com.example.springrestapi.interestpoints.controller;

import com.example.springrestapi.interestpoints.model.Category;
import com.example.springrestapi.interestpoints.model.Place;
import com.example.springrestapi.interestpoints.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaceRepositoryTest {

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceRepositoryTest placeRepositoryTest;

    @Test
    public void testFindByIsPublicTrue() {
        Place place1 = new Place();
        place1.setIsPublic(true);
        Place place2 = new Place();
        place2.setIsPublic(true);

        when(placeRepository.findByIsPublicTrue()).thenReturn(Arrays.asList(place1, place2));

        List<Place> publicPlaces = placeRepository.findByIsPublicTrue();
        assertThat(publicPlaces).isNotEmpty();
    }

    @Test
    public void testFindByCategoryIdAndIsPublicTrue() {
        Long categoryId = 2L;
        Place place = new Place();
        Category category = new Category();
        category.setId(categoryId);
        place.setCategory(category);
        place.setIsPublic(true);

        when(placeRepository.findByCategoryIdAndIsPublicTrue(categoryId)).thenReturn(Arrays.asList(place));

        List<Place> places = placeRepository.findByCategoryIdAndIsPublicTrue(categoryId);
        assertThat(places).isNotEmpty();
    }

    @Test
    public void testFindByUserId() {
        Long userId = 3L;
        Place place = new Place();
        place.setUserId(userId);

        when(placeRepository.findByUserId(userId)).thenReturn(Arrays.asList(place));

        List<Place> places = placeRepository.findByUserId(userId);
        assertThat(places).isNotEmpty();
    }

    @Test
    public void testFindWithinRadius() {
        double lat = 40.7128;
        double lon = -74.0060;
        double radius = 1000; // Radius in meters
        Place place = new Place();

        when(placeRepository.findWithinRadius(lat, lon, radius)).thenReturn(Arrays.asList(place));

        List<Place> places = placeRepository.findWithinRadius(lat, lon, radius);
        assertThat(places).isNotEmpty();
    }

    @Test
    public void testFindByIdAndIsPublicTrue() {
        Long placeId = 4L;
        Place place = new Place();
        place.setId(placeId);
        place.setIsPublic(true);

        when(placeRepository.findByIdAndIsPublicTrue(placeId)).thenReturn(Optional.of(place));

        Optional<Place> foundPlace = placeRepository.findByIdAndIsPublicTrue(placeId);
        assertThat(foundPlace).isPresent();
    }
}