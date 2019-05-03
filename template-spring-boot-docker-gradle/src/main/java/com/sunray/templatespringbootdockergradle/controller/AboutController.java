package com.sunray.templatespringbootdockergradle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutController {
	@Value("${app.message.about}")
	private String messageAbout;
	public static final Logger logger = LoggerFactory.getLogger(AboutController.class.getName());
	  @GetMapping(value = "/api/about")
	  @CrossOrigin(origins = "*") 
	  public String about() {
		  final String aboutMessage =  "Version 1.0 " + messageAbout;
		  logger.info(aboutMessage);
		  return aboutMessage;
	  }
}
