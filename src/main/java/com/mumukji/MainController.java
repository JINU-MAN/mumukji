package com.mumukji;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
public class MainController {
	@GetMapping("/")
	public String mainPage() {
		return "redirect:/login";
	}
}
