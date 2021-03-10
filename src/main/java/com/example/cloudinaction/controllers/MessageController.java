package com.example.cloudinaction.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MessageController
{
	@Value("${test.message}")
	private String message;

	@GetMapping("/getMessage")
	public String getMessage(){
		return message;
	}
}