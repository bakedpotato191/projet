package com.example.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.apierror.ApiError;

@RestController
public class OverrideErrorWhitePage implements ErrorController {
	
	@RequestMapping(value = "/error" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiError handleError() {
		return new ApiError(HttpStatus.NOT_FOUND, "Requested resource was not found on this server");
	}
}
