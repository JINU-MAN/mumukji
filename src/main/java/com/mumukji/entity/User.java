package com.mumukji.entity;

import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userId;
	
	@Enumerated(EnumType.STRING)
	private Ingredient ingredient;
	
	@Enumerated(EnumType.STRING)
	private CookingMethod cookingMethod;
	
	 @ElementCollection(fetch = FetchType.EAGER)
	 @Enumerated(EnumType.STRING)
	 private Set<FoodKeyword> keywords;
	
	public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setCookingMethod(CookingMethod cookingMethod) {
        this.cookingMethod = cookingMethod;
    }

    public void setKeywords(Set<FoodKeyword> keywords) {
        this.keywords = keywords;
    }

}
