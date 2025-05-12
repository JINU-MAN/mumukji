package com.mumukji.util;

import com.mumukji.entity.*;
import com.mumukji.repository.FoodRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserPreferenceMapper {

    private final FoodRepository foodRepository;

    public UserPreferenceMapper(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    /**
     * 사용자 선호 응답(Map<음식 이름, 점수>)을 기반으로 User 엔터티의 속성값을 설정
     */
    public void applyPreferenceToUser(User user, Map<String, Integer> responses) {
    	System.out.println("[DEBUG] responses: " + responses);
    	
        Map<Ingredient, Integer> ingredientScore = new EnumMap<>(Ingredient.class);
        Map<CookingMethod, Integer> methodScore = new EnumMap<>(CookingMethod.class);
        Map<FoodKeyword, Integer> keywordScore = new EnumMap<>(FoodKeyword.class);
        int totalScore = 0;

        for (Map.Entry<String, Integer> entry : responses.entrySet()) {
            String foodName = entry.getKey();
            int score = entry.getValue();

            Optional<Food> optionalFood = foodRepository.findByName(foodName);
            if (optionalFood.isEmpty()) continue;

            Food food = optionalFood.get();
            totalScore += score;

            ingredientScore.merge(food.getIngredient(), score, Integer::sum);
            methodScore.merge(food.getCookingMethod(), score, Integer::sum);
            for (FoodKeyword keyword : food.getKeywords()) {
                keywordScore.merge(keyword, score, Integer::sum);
            }
        }

        if (totalScore == 0) return; // 데이터 없음

        // 가장 높은 점수 속성으로 선택 (단순화된 방식)
        Ingredient topIngredient = ingredientScore.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        CookingMethod topMethod = methodScore.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        Set<FoodKeyword> topKeywords = new HashSet<>();
        keywordScore.entrySet().stream()
                .sorted(Map.Entry.<FoodKeyword, Integer>comparingByValue().reversed())
                .limit(3)
                .forEach(e -> topKeywords.add(e.getKey()));
        
        user.setIngredient(topIngredient);
        user.setCookingMethod(topMethod);
        user.setKeywords(topKeywords);
        System.out.println("ingredient: " + topIngredient);
        System.out.println("method: " + topMethod);	
        System.out.println("keywords: " + topKeywords);
    }
}
