package com.sunray.templatespringbootdockergradle.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sunray.templatespringbootdockergradle.service.HealthService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HealthServiceTest {
	@Autowired
	private HealthService healthService;

	@Test
	public void testGetAboutMessage() {
		assertEquals(healthService.getAboutMessage(), "Version 1.0 Running in compose mode");
	}

}
