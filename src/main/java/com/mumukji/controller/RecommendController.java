package com.mumukji.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mumukji.service.RecommendService;
import com.mumukji.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@Getter
@Setter
@RequiredArgsConstructor
public class RecommendController {
	private final UserService userService;
	private final RecommendService recommendService;
	
	@GetMapping("/makePref")
	public String makePref(HttpSession session) {
		Map<String, Integer> pref = (Map<String,Integer>)session.getAttribute("responses");
		String userId =(String) session.getAttribute("userId");
		userService.updateUserPreference(userId, pref);
		return "redirect:/recommend";
	}
	
	@GetMapping("/recommend")
	public String showRecommended(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
	    List<String> recommendedNames = recommendService.recommendByPreference(userId);

	    model.addAttribute("recommendedNames", recommendedNames);
		return "recommend";
	}

}
