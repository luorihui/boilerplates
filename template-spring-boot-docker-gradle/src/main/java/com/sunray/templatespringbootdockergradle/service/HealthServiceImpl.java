package com.sunray.templatespringbootdockergradle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HealthServiceImpl implements HealthService {
	@Value("${app.message.about}")
	private String messageAbout;
	
	@Override
	public String getAboutMessage() {
		return "Version 1.0 " + messageAbout;
	}

}
