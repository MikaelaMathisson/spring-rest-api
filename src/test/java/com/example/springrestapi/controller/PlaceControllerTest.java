package com.example.springrestapi.controller;

import com.example.springrestapi.model.Place;
import com.example.springrestapi.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceControllerTest {

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceController placeController;

    @Test
    void getPublicPlaces_returnsPublicPlaces() {
        List<Place> places = List.of(new Place());
        when(placeRepository.findByIsPublicTrueAndIsDeletedFalse()).thenReturn(places);

        List<Place> result = placeController.getPublicPlaces();

        assertEquals(places, result);
    }

    @Test
    void getUserPlaces_returnsUserPlaces() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");
        List<Place> places = List.of(new Place());
        when(placeRepository.findByUserIdAndIsDeletedFalse(1L)).thenReturn(places);

        List<Place> result = placeController.getUserPlaces(jwt);

        assertEquals(places, result);
    }

    @Test
    void getUserPlaces_noJwt_returnsEmptyList() {
        List<Place> result = placeController.getUserPlaces(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void getPlaceById_existingId_returnsPlace() {
        Place place = new Place();
        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));

        ResponseEntity<Place> result = placeController.getPlaceById(1L);

        assertEquals(ResponseEntity.ok(place), result);
    }

    @Test
    void getPlaceById_nonExistingId_returnsNotFound() {
        when(placeRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Place> result = placeController.getPlaceById(1L);

        assertEquals(ResponseEntity.notFound().build(), result);
    }

    @Test
    void getPlacesByCategory_returnsPlaces() {
        List<Place> places = List.of(new Place());
        when(placeRepository.findByCategoryIdAndIsPublicTrueAndIsDeletedFalse(1L)).thenReturn(places);

        List<Place> result = placeController.getPlacesByCategory(1L);

        assertEquals(places, result);
    }

    @Test
    void getPlacesWithinRadius_returnsPlaces() {
        List<Place> places = List.of(new Place());
        when(placeRepository.findPublicPlacesWithinRadius("point", 10.0)).thenReturn(places);

        List<Place> result = placeController.getPlacesWithinRadius("point", 10.0);

        assertEquals(places, result);
    }

    @Test
    void createPlace_asAdmin_createsPlace() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");
        Place place = new Place();
        when(placeRepository.save(place)).thenReturn(place);

        ResponseEntity<Place> result = placeController.createPlace(place, jwt);

        assertEquals(ResponseEntity.ok(place), result);
    }

    @Test
    void createPlace_noJwt_returnsForbidden() {
        Place place = new Place();

        ResponseEntity<Place> result = placeController.createPlace(place, null);

        assertEquals(ResponseEntity.status(403).build(), result);
    }

    @Test
    void updatePlace_asUser_updatesPlace() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");
        Place existingPlace = new Place();
        existingPlace.setUserId(1L);
        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        when(placeRepository.save(existingPlace)).thenReturn(existingPlace);
        Place placeDetails = new Place();
        placeDetails.setName("Updated Place");

        ResponseEntity<?> result = placeController.updatePlace(1L, placeDetails, jwt);

        assertEquals(ResponseEntity.ok(existingPlace), result);
    }

    @Test
    void updatePlace_noJwt_returnsForbidden() {
        Place placeDetails = new Place();

        ResponseEntity<?> result = placeController.updatePlace(1L, placeDetails, null);

        assertEquals(ResponseEntity.status(403).build(), result);
    }

    @Test
    void updatePlace_nonExistingPlace_returnsNotFound() {
        Jwt jwt = mock(Jwt.class);
        when(placeRepository.findById(1L)).thenReturn(Optional.empty());
        Place placeDetails = new Place();

        ResponseEntity<?> result = placeController.updatePlace(1L, placeDetails, jwt);

        assertEquals(ResponseEntity.notFound().build(), result);
    }

    @Test
    void deletePlace_asAdmin_deletesPlace() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");
        Place existingPlace = new Place();
        existingPlace.setUserId(1L);
        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));

        ResponseEntity<Object> result = placeController.deletePlace(1L, jwt);

        assertEquals(ResponseEntity.ok().build(), result);
        assertTrue(existingPlace.isDeleted());
    }

    @Test
    void deletePlace_noJwt_returnsForbidden() {
        ResponseEntity<Object> result = placeController.deletePlace(1L, null);

        assertEquals(ResponseEntity.status(403).build(), result);
    }

    @Test
    void deletePlace_nonExistingPlace_returnsNotFound() {
        Jwt jwt = mock(Jwt.class);
        when(placeRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> result = placeController.deletePlace(1L, jwt);

        assertEquals(ResponseEntity.notFound().build(), result);
    }
}