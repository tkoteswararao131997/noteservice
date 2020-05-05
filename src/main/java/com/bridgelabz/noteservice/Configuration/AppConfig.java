package com.bridgelabz.noteservice.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.noteservice.Entity.NoteEntity;
import com.bridgelabz.noteservice.Utility.JwtOperations;
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
