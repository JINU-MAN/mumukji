package com.mumukji.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mumukji.entity.Food;


@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{
	Optional<Food> findByName(String name);
	boolean existsByName(String name);
}
