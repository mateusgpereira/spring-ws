package com.springws.app.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.springws.app.ui.model.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionsHandler {
	
	@ExceptionHandler(value = {UserServiceException.class})
	public ResponseEntity<Object> handleUserException(UserServiceException ex, WebRequest req) {
		ErrorMessage error = new ErrorMessage(new Date(), ex.getMessage());
		return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		
		return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

}
