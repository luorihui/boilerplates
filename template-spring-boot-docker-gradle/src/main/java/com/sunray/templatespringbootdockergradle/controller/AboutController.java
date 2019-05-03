package com.sunray.templatespringbootdockergradle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutController {
	public static final Logger logger = LoggerFactory.getLogger(AboutController.class.getName());
	  @GetMapping(value = "/api/about")
	  @CrossOrigin(origins = "*") 
	  public String about() {
		  final String aboutMessage =  "Sunray Version 1.0";
		  logger.info(aboutMessage);
		  return aboutMessage;
	  }
}
