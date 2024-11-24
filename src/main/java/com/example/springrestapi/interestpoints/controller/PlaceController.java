package com.example.springrestapi.interestpoints.controller;

import com.example.springrestapi.interestpoints.model.Place;
import com.example.springrestapi.interestpoints.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeRepository.findByIsPublicTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        return placeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public List<Place> getPlacesByCategory(@PathVariable Long categoryId) {
        return placeRepository.findByCategoryIdAndIsPublicTrue(categoryId);
    }

    @GetMapping("/user")
    public List<Place> getUserPlaces(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return placeRepository.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Place place, @AuthenticationPrincipal Jwt jwt) {
        place.setUserId(Long.valueOf(jwt.getSubject()));
        return ResponseEntity.ok(placeRepository.save(place));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable Long id, @RequestBody Place placeDetails, @AuthenticationPrincipal Jwt jwt) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlace(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return placeRepository.findById(id)
                .map(place -> {
                    if (!place.getUserId().equals(Long.valueOf(jwt.getSubject()))) {
                        return ResponseEntity.status(403).build();
                    }
                    placeRepository.delete(place);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}