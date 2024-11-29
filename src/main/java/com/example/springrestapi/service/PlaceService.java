package com.example.springrestapi.service;

import com.example.springrestapi.model.Place;
import com.example.springrestapi.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {
    @Autowired
    private PlaceRepository placeRepository;

    public List<Place> getAllPlacesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String username = authentication.getName();
            return placeRepository.findByIsPublicTrueOrUserId(username);
        } else {
            return placeRepository.findByIsPublicTrueAndIsDeletedFalse();
        }
    }

    public Optional<Place> getPublicPlaceById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            return placeRepository.findById(id);
        } else {
            return placeRepository.findById(id)
                    .filter(Place::isPublic);
        }
    }

    public List<Place> getPublicPlacesByCategory(Long categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String userId = authentication.getName();
            return placeRepository.findByCategoryIdAndIsPublicTrueOrUserId(categoryId, userId);
        } else {
            return placeRepository.findByCategoryIdAndIsPublicTrueAndIsDeletedFalse(categoryId);
        }
    }

    public List<Place> getUserPlaces(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getName().equals(userId.toString())) {
            return placeRepository.findByUserIdAndIsDeletedFalse(userId);
        } else {
            throw new SecurityException("You are not authorized to access this resource");
        }
    }

    public List<Place> getPlacesWithinRadius(double latitude, double longitude, double radius) {
        String point = String.format("POINT(%f %f)", longitude, latitude);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String username = authentication.getName();
            return placeRepository.findPlacesWithinRadiusForUser(point, radius, username);
        } else {
            return placeRepository.findPublicPlacesWithinRadius(point, radius);
        }
    }

    public Place createPlace(Place place) {
        if (place.getId() != null && placeRepository.existsById(place.getId())) {
            throw new IllegalArgumentException("Place with this ID already exists");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String userId = authentication.getName();
            place.setUserId(Long.valueOf(userId));
            place.setCreatedAt(LocalDateTime.now());
            place.setLastModified(LocalDateTime.now());
            return placeRepository.save(place);
        } else {
            throw new SecurityException("You are not authorized to create a place");
        }
    }

    public Place updatePlace(Long id, Place placeDetails) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        place.setName(placeDetails.getName());
        place.setCategory(placeDetails.getCategory());
        place.setDescription(placeDetails.getDescription());
        place.setCoordinates(placeDetails.getCoordinates());
        place.setLocation(placeDetails.getLocation());
        place.setPublic(placeDetails.isPublic());
        place.setLastModified(LocalDateTime.now());
        return placeRepository.save(place);
    }

    public void deletePlace(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        place.setDeleted(true);
        placeRepository.save(place);
    }
}