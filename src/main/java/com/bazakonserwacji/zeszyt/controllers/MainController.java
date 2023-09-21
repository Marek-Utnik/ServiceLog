package com.bazakonserwacji.zeszyt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.services.SystemUserService;


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
	    		@ModelAttribute SystemUser systemUser) {
  			model.addAttribute("comment", "New user added");
	        systemUserService.saveSystemUser(systemUser);
	        return "login";
  }	  
  
}