package com.mumukji.loader;

import com.mumukji.entity.*;
import com.mumukji.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FoodCsvLoader {

    @Autowired
    private FoodRepository foodRepository;

    @PostConstruct
    public void loadFoodData() throws IOException {
        InputStream is = getClass().getResourceAsStream("/foods.csv");
        if (is == null) {
            throw new FileNotFoundException("foods.csv not found in resources");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String name = parts[0].trim();
                Ingredient ingredient = Ingredient.valueOf(parts[1].trim());
                CookingMethod method = CookingMethod.valueOf(parts[2].trim());
                Set<FoodKeyword> keywords = Arrays.stream(parts[3].split("\\|"))
                                                  .map(String::trim)
                                                  .map(FoodKeyword::valueOf)
                                                  .collect(Collectors.toSet());
                Set<FoodCategory> category = Arrays.stream(parts[4].split("\\;"))
                								  .map(String::trim).map(FoodCategory::valueOf).collect(Collectors.toSet())
                								  ;
                
                Food food;
                if (foodRepository.existsByName(name)) {
                    food = foodRepository.findByName(name)
                    		.orElseThrow(()->new IllegalStateException("FoodNotFound"));
                    food.setCategory(category); // 기존 엔티티에 추가
                } else {
                    food = new Food();
                    food.setName(name);
                    food.setIngredient(ingredient);
                    food.setCookingMethod(method);
                    food.setKeywords(keywords);
                    food.setCategory(category);
                }
                foodRepository.save(food); // 항상 save

                System.out.println("Saving: " + food.getName() + ", categories: " + food.getCategory());
                if (!foodRepository.existsByName(food.getName())) {
                    foodRepository.save(food);
                }
            }
        }
    }
}
