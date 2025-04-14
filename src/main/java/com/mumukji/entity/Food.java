package com.mumukji.entity;

import jakarta.persistence.*;
import java.util.*;

import lombok.*;

@Getter
@Setter
@Entity
public class Food {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Ingredient ingredient;
	
	@Enumerated(EnumType.STRING)
	private CookingMethod cookingMethod;
	
	 @ElementCollection(fetch = FetchType.EAGER)
	 @Enumerated(EnumType.STRING)
	 private Set<FoodKeyword> keywords;
	
}
