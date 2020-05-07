package com.bridgelabz.noteservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.noteservice.entity.NoteEntity;
import com.bridgelabz.noteservice.utility.JwtOperations;
@Component
public class AppConfig {
	@Bean
	public JwtOperations jwtoperations()
	{
		return new JwtOperations();
	}
	@Bean
	public NoteEntity noteentity()
	{
		return new NoteEntity();
	}
	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
}
