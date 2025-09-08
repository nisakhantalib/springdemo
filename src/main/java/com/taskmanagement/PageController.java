package com.taskmanagement;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	@GetMapping({"/", "/home"})
	public String home(){
		return "home";
	}
	
	@GetMapping("/login")
	public String login(){
		return "login";
	}
	
	@GetMapping("/hello")
	public String hello(Model model, Authentication authentication){
		String username;
		if(authentication.getPrincipal() instanceof OAuth2User oAuthUser) {
			username=oAuthUser.getAttribute("email");
		}
		else {
			username=authentication.getName();
		}
		model.addAttribute("username",username);
		return "hello";
	}

}
