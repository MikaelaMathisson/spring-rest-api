package com.example.springrestapi.controller;

import com.example.springrestapi.model.Place;
import com.example.springrestapi.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping("/public")
    public List<Place> getPublicPlaces() {
        return placeRepository.findByIsPublicTrueAndIsDeletedFalse();
    }

    @PreAuthorize("hasAuthority('SCOPE_user')")
    @GetMapping
    public List<Place> getUserPlaces(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return List.of();
        }
        Long userId = Long.valueOf(jwt.getSubject());
        return placeRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        return placeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public List<Place> getPlacesByCategory(@PathVariable Long categoryId) {
        return placeRepository.findByCategoryIdAndIsPublicTrueAndIsDeletedFalse(categoryId);
    }

    @PreAuthorize("hasAuthority('SCOPE_admin')")
    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Place place, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(403).build();
        }
        place.setUserId(Long.valueOf(jwt.getSubject()));
        return ResponseEntity.ok(placeRepository.save(place));
    }

    @PreAuthorize("hasAuthority('SCOPE_user')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable Long id, @RequestBody Place placeDetails, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(403).build();
        }
        return placeRepository.findById(id)
                .map(place -> {
                    if (!place.getUserId().equals(Long.valueOf(jwt.getSubject()))) {
                        return ResponseEntity.status(403).build();
                    }
                    place.setName(placeDetails.getName());
                    place.setCategory(placeDetails.getCategory());
                    place.setPublic(placeDetails.isPublic());
                    place.setDescription(placeDetails.getDescription());
                    place.setCoordinates(placeDetails.getCoordinates());
                    return ResponseEntity.ok(placeRepository.save(place));
                })
                .orElse(ResponseEntity.notFound().build());
    }
@PreAuthorize("hasAuthority('SCOPE_admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlace(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(403).build();
        }
        return placeRepository.findById(id)
                .map(place -> {
                    if (!place.getUserId().equals(Long.valueOf(jwt.getSubject()))) {
                        return ResponseEntity.status(403).build();
                    }
                    place.setDeleted(true);
                    placeRepository.save(place);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
