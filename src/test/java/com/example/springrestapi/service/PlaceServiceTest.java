package com.example.springrestapi.service;

import com.example.springrestapi.model.Place;
import com.example.springrestapi.repository.PlaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PlaceService placeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllPlacesForUser_authenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user1");
        placeService.getAllPlacesForUser();
        verify(placeRepository, times(1)).findByIsPublicTrueOrUserId("user1");
    }

    @Test
    void getAllPlacesForUser_anonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        placeService.getAllPlacesForUser();
        verify(placeRepository, times(1)).findByIsPublicTrueAndIsDeletedFalse();
    }

    @Test
    void getPublicPlaceById_authenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user1");
        when(placeRepository.findById(1L)).thenReturn(Optional.of(new Place()));
        Optional<Place> place = placeService.getPublicPlaceById(1L);
        assertTrue(place.isPresent());
        verify(placeRepository, times(1)).findById(1L);
    }

    @Test
    void getPublicPlaceById_anonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        Place publicPlace = new Place();
        publicPlace.setPublic(true);
        when(placeRepository.findById(1L)).thenReturn(Optional.of(publicPlace));
        Optional<Place> place = placeService.getPublicPlaceById(1L);
        assertTrue(place.isPresent());
        verify(placeRepository, times(1)).findById(1L);
    }

    @Test
    void createPlace_authenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("1");
        Place place = new Place();
        place.setName("New Place");
        when(placeRepository.save(place)).thenReturn(place);
        Place createdPlace = placeService.createPlace(place);
        assertNotNull(createdPlace);
        assertEquals("1", createdPlace.getUserId().toString());
        verify(placeRepository, times(1)).save(place);
    }

    @Test
    void createPlace_anonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        Place place = new Place();
        assertThrows(SecurityException.class, () -> placeService.createPlace(place));
        verify(placeRepository, never()).save(place);
    }

    @Test
    void updatePlace_existingPlace() {
        Place existingPlace = new Place();
        existingPlace.setId(1L);
        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        Place placeDetails = new Place();
        placeDetails.setName("Updated Place");
        placeDetails.setCategory("Updated Category");
        placeDetails.setDescription("Updated Description");
        placeDetails.setCoordinates("Updated Coordinates");
        placeDetails.setLocation("Updated Location");
        placeDetails.setPublic(true);
        when(placeRepository.save(existingPlace)).thenReturn(existingPlace);

        Place updatedPlace = placeService.updatePlace(1L, placeDetails);
        assertEquals("Updated Place", updatedPlace.getName());
        assertEquals("Updated Category", updatedPlace.getCategory());
        assertEquals("Updated Description", updatedPlace.getDescription());
        assertEquals("Updated Coordinates", updatedPlace.getCoordinates());
        assertEquals("Updated Location", updatedPlace.getLocation());
        assertTrue(updatedPlace.isPublic());
        verify(placeRepository, times(1)).save(existingPlace);
    }

    @Test
    void deletePlace_existingPlace() {
        Place existingPlace = new Place();
        existingPlace.setId(1L);
        when(placeRepository.findById(1L)).thenReturn(Optional.of(existingPlace));
        placeService.deletePlace(1L);
        assertTrue(existingPlace.isDeleted());
        verify(placeRepository, times(1)).save(existingPlace);
    }
}