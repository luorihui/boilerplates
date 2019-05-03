package com.sunray.templatespringbootdockergradle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunray.templatespringbootdockergradle.service.HealthService;

@RestController
public class AboutController {
	@Autowired
	private HealthService healthService;
	
	public static final Logger logger = LoggerFactory.getLogger(AboutController.class.getName());
	  @GetMapping(value = "/about")
	  @CrossOrigin(origins = "*") 
	  public String about() {
		  return healthService.getAboutMessage();
	  }
}
