package com.mumukji.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mumukji.entity.Food;
import com.mumukji.entity.FoodCategory;


@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{
	Optional<Food> findByName(String name);
	boolean existsByName(String name);
	List<Food> findByCategory(FoodCategory category);
	List<Food> findByNameIn(List<String> foodList);
}
