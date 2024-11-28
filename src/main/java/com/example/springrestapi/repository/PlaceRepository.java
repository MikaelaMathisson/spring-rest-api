package com.example.springrestapi.repository;

import com.example.springrestapi.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByIsPublicTrueAndIsDeletedFalse();
    List<Place> findByCategoryIdAndIsPublicTrueAndIsDeletedFalse(Long categoryId);
    List<Place> findByUserIdAndIsDeletedFalse(Long userId);

    @Query("SELECT p FROM Place p WHERE p.category.id = :categoryId AND (p.isPublic = true OR p.userId = :userId)")
    List<Place> findByCategoryIdAndIsPublicTrueOrUserId(@Param("categoryId") Long categoryId, @Param("userId") String userId);

    @Query("SELECT p FROM Place p WHERE ST_Distance_Sphere(p.coordinates, ST_GeomFromText(:point, 4326)) <= :radius AND p.isPublic = true")
    List<Place> findPublicPlacesWithinRadius(@Param("point") String point, @Param("radius") double radius);

    @Query("SELECT p FROM Place p WHERE ST_Distance_Sphere(p.coordinates, ST_GeomFromText(:point, 4326)) <= :radius AND (p.isPublic = true OR p.userId = :userId)")
    List<Place> findPlacesWithinRadiusForUser(@Param("point") String point, @Param("radius") double radius, @Param("userId") String userId);

    @Query("SELECT p FROM Place p WHERE p.isPublic = true OR p.userId = :userId")
    List<Place> findByIsPublicTrueOrUserId(@Param("userId") String userId);
}