package com.example.springrestapi.repository;

import com.example.springrestapi.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByIsPublicTrueAndIsDeletedFalse();
    List<Place> findByCategoryIdAndIsPublicTrueAndIsDeletedFalse(Long categoryId);
    List<Place> findByUserIdAndIsDeletedFalse(Long userId);
/*
    @Query("SELECT p FROM Place p WHERE ST_DWithin(p.coordinates, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326), :radius) AND p.isDeleted = false")
    List<Place> findWithinRadius(@Param("lat") double lat, @Param("lon") double lon, @Param("radius") double radius);
*/
    Optional<Place> findByIdAndIsPublicTrueAndIsDeletedFalse(Long id);
}