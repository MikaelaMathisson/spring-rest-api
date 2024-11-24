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
        place1.setPublic(true);
        Place place2 = new Place();
        place2.setPublic(true);

        when(placeRepository.findByIsPublicTrueAndIsDeletedFalse()).thenReturn(Arrays.asList(place1, place2));

        List<Place> publicPlaces = placeRepository.findByIsPublicTrueAndIsDeletedFalse();
        assertThat(publicPlaces).isNotEmpty();
    }

    @Test
    public void testFindByCategoryIdAndIsPublicTrue() {
        Long categoryId = 2L;
        Place place = new Place();
        Category category = new Category();
        category.setId(categoryId);
        place.setCategory(category);
        place.setPublic(true);

        when(placeRepository.findByCategoryIdAndIsPublicTrueAndIsDeletedFalse(categoryId)).thenReturn(Arrays.asList(place));

        List<Place> places = placeRepository.findByCategoryIdAndIsPublicTrueAndIsDeletedFalse(categoryId);
        assertThat(places).isNotEmpty();
    }

    @Test
    public void testFindByUserId() {
        Long userId = 3L;
        Place place = new Place();
        place.setUserId(userId);

        when(placeRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Arrays.asList(place));

        List<Place> places = placeRepository.findByUserIdAndIsDeletedFalse(userId);
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
        place.setPublic(true);

        when(placeRepository.findByIdAndIsPublicTrueAndIsDeletedFalse(placeId)).thenReturn(Optional.of(place));

        Optional<Place> foundPlace = placeRepository.findByIdAndIsPublicTrueAndIsDeletedFalse(placeId);
        assertThat(foundPlace).isPresent();
    }
}