package com.servicedata.servicelogs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.services.SystemUserService;

import jakarta.validation.Valid;


@Controller
public class MainController {

  
  private final SystemUserService systemUserService;

  public MainController(SystemUserService systemUserService) {
      this.systemUserService = systemUserService;
  }
  
  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/contact")
  public String contact() {
	return "contact";
  }

  @GetMapping("/login")  
  public String login() {
	  return "login";
  }
  
  @GetMapping("/register")  
  public String registerForm(Model model) {

	  
	  SystemUser systemUser = new SystemUser();
	  model.addAttribute(systemUser);
	  return "register";
  }
  
  @PostMapping("/new-user")
	    public String addNewSystemUser(Model model,
	    		@Valid SystemUser systemUser,
		  		Errors errors
	    		) {
	  		if (errors.hasErrors()) {
	  			model.addAttribute(systemUser);
	  			return "register";
	  		}
  			model.addAttribute("comment", "New user added");
	        systemUserService.saveSystemUser(systemUser);
	        return "login";
  }	  
  
}