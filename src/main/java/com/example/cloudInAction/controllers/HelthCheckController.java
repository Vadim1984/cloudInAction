package com.example.cloudInAction.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class HelthCheckController
{
	@GetMapping("/healthCheck")
	public String healthCheck(){
		return "Hello, current time is : " + new Date().toString();
	}
}