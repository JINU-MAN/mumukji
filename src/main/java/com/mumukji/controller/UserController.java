package com.mumukji.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mumukji.entity.User;
import com.mumukji.repository.UserRepository;
import com.mumukji.service.UserService;
import com.mumukji.exception.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;
	private final UserRepository userRepository;
	
	@GetMapping("/login")
	public String login(HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		if(userId == null) {
			return "login_form";
		} else {
			//return("redirect:/survey");
			return "login_form";
		}
		
	}
	@PostMapping("/login")
	public String login(@RequestParam("userId") String userId,
	                    HttpServletRequest request,Model model) {
	    HttpSession oldSession = request.getSession(false);
	    if (oldSession != null) {
	        oldSession.invalidate(); // 기존 세션 무효화
	    }

	    HttpSession newSession = request.getSession(true); // 새 세션 생성
	    try {
	    	userService.loginorregisterUser(userId);
	    	newSession.setAttribute("userId", userId); // 새 세션에 저장
	    	if(userId == "00000000") {
	    		return "redirect:/survey_situation";
	    	}
	 	    return "redirect:/survey_situation";
	    }catch(InvalidFormatException e){
	    	model.addAttribute("errorMessage", e.getMessage());
	    	return "login_form";
	    }
	}
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
