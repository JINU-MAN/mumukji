package com.mumukji.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mumukji.entity.User;
import com.mumukji.repository.UserRepository;
import com.mumukji.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	Set<String> tempId = Set.of(
		    "00000000", "12345678", "11111111", "22222222",
		    "33333333", "44444444", "55555555", "66666666",
		    "77777777", "88888888", "99999999"
		);
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
	                    HttpServletRequest request) {
	    HttpSession oldSession = request.getSession(false);
	    if (oldSession != null) {
	        oldSession.invalidate(); // 기존 세션 무효화
	    }

	    HttpSession newSession = request.getSession(true); // 새 세션 생성
	    if (tempId.contains(userId)) {
	        userId = "00000000";
	    }

	    if (!userRepository.existsByUserId(userId)) {
	        User newUser = new User();
	        newUser.setUserId(userId);
	        userRepository.save(newUser);
	    }

	    newSession.setAttribute("userId", userId); // 새 세션에 저장
	    return "redirect:/survey/0";
	}
}
