package com.example.springrestapi.interestpoints.repository;

import com.example.springrestapi.interestpoints.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}