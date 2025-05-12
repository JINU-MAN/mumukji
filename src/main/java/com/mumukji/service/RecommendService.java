package com.mumukji.service;

import com.mumukji.entity.*;
import com.mumukji.repository.FoodRepository;
import com.mumukji.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendService {
	private static final int INGREDIENT_OFFSET = 0;
	private static final int METHOD_OFFSET = INGREDIENT_OFFSET + Ingredient.values().length;
	private static final int KEYWORD_OFFSET = METHOD_OFFSET + CookingMethod.values().length;
	private static final int VECTOR_SIZE = KEYWORD_OFFSET + FoodKeyword.values().length;

	

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public RecommendService(UserRepository userRepository, FoodRepository foodRepository) {
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }
    
    public List<String> recommendByPreference(String userId,String category) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        double[] userVector = toVector(user);
        FoodCategory c;
        c = FoodCategory.valueOf(category);
        return foodRepository.findByCategory(c).stream()
                .map(food -> Map.entry(food, cosineSimilarity(userVector, toVector(food))))
                .sorted(Map.Entry.<Food, Double>comparingByValue().reversed())
                .map(entry -> entry.getKey().getName())
                .collect(Collectors.toList());
    }

    private double[] toVector(User user) {
	    double[] vector = new double[VECTOR_SIZE];

	    if (user.getIngredient() != null)
	        vector[INGREDIENT_OFFSET + user.getIngredient().ordinal()] = 1.0;

	    if (user.getCookingMethod() != null)
	        vector[METHOD_OFFSET + user.getCookingMethod().ordinal()] = 1.0;

	    if (user.getKeywords() != null)
	        for (FoodKeyword keyword : user.getKeywords())
	            vector[KEYWORD_OFFSET + keyword.ordinal()] = 1.0;

	    return vector;
	}

    private double[] toVector(Food food) {
    	double[] vector = new double[VECTOR_SIZE];

	    if (food.getIngredient() != null)
	        vector[INGREDIENT_OFFSET + food.getIngredient().ordinal()] = 1.0;

	    if (food.getCookingMethod() != null)
	        vector[METHOD_OFFSET + food.getCookingMethod().ordinal()] = 1.0;

	    if (food.getKeywords() != null)
	        for (FoodKeyword keyword : food.getKeywords())
	            vector[KEYWORD_OFFSET + keyword.ordinal()] = 1.0;

	    return vector;
    }
    

    private double cosineSimilarity(double[] a, double[] b) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0.0 || normB == 0.0) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
} 
