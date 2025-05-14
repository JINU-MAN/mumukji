package com.mumukji.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mumukji.entity.Food;
import com.mumukji.repository.FoodRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class SurveyController {
	@Autowired
	FoodRepository foodRepository;
	
	private final List<Map<String, String>> surveyItems = List.of(
		    Map.of("name", "피자", "imageUrl", "/images/pizza.png"),
		    Map.of("name", "초밥", "imageUrl", "/images/sushi.png"),
		    Map.of("name", "샐러드", "imageUrl", "/images/salad.png"),
		    Map.of("name", "치킨", "imageUrl", "/images/chicken.png"),
		    Map.of("name", "된장찌개", "imageUrl", "/images/soybean_soup.png"),
		    Map.of("name", "떡볶이", "imageUrl", "/images/tteokbokki.png"),
		    Map.of("name", "크림파스타", "imageUrl", "/images/cream_pasta.png")
		);

	
	
	@GetMapping("/survey/{page}")
	public String surveyPage(@PathVariable("page") int page, Model model){
		if (page < 0) return "redirect:/survey/0";
		if(page >= surveyItems.size()) return "redirect:/makePref";

        Map<String, String> food = surveyItems.get(page);

        model.addAttribute("food", food);
        model.addAttribute("currentPage", page);
        model.addAttribute("isLastPage", page == surveyItems.size() - 1);

        return "survey";
	}
	@PostMapping("/survey/{page}")
	public String answerCtrl(@PathVariable("page") int page, @RequestParam("food") String food, @RequestParam("score") int score,
							HttpSession session) 
	{
		Map<String,Integer> responses = (Map<String, Integer>) session.getAttribute("responses");
		if(responses == null) {
			responses = new LinkedHashMap<>();
		}
	
		responses.put(food, score);
		session.setAttribute("responses", responses);
		return "redirect:/survey/" + (page+1);
	}
	
	@RequestMapping(value="/survey_situation", method=RequestMethod.GET)
	public String toSelectSituationPage(HttpSession session) {
		return "survey_situation";
	}
	@RequestMapping(value="/survey_situation",method=RequestMethod.POST)
	public String selectSituation(HttpSession session, @RequestParam("category") String category) {
		//TODO: process POST request
		if(category.equals("CHOICE_PROBLEM")) {
			session.setAttribute("category", category);
			return "redirect:/vs_situation";
		}
		session.setAttribute("category", category);
		return "redirect:/survey/0";
	}
	@RequestMapping(value="/vs_situation", method=RequestMethod.GET)
	public String vsPageShow(Model model) {
		List<Food> foodList = foodRepository.findAll();
		model.addAttribute("foodList",foodList);
		return "vs_situation";
	}
	@RequestMapping(value="/vs_situation",method=RequestMethod.POST)
	public String choiceHandler(HttpSession session,@RequestParam("foods") List<String> selectedFood) {
		session.setAttribute("selectedFood", selectedFood);
		return "redirect:/survey/0";
	}
	
}

