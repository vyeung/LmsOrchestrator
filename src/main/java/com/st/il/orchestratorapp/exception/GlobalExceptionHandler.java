package com.st.orchestrator.exception;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	//the custom exceptions
	@ExceptionHandler({NotFoundException.class, BadRequestException.class, AlreadyExistsException.class})
	public ResponseEntity<ApiError> handleRegularExceptions(Exception ex, WebRequest request) {
		
		if(ex instanceof NotFoundException) {
			ApiError apiE = new ApiError(404, "NOT FOUND", ex.getMessage());
			return new ResponseEntity<>(apiE, HttpStatus.NOT_FOUND);
		}
		else if(ex instanceof BadRequestException) {
			ApiError apiE = new ApiError(400, "BAD REQUEST", ex.getMessage());
			return new ResponseEntity<>(apiE, HttpStatus.BAD_REQUEST);
		}
		else if(ex instanceof AlreadyExistsException) {
			ApiError apiE = new ApiError(409, "CONFLICT", ex.getMessage());
			return new ResponseEntity<>(apiE, HttpStatus.CONFLICT);
		}
		else {
			ApiError apiE = new ApiError(500, "INTERNAL_SERVER_ERROR", "All hope is lost");
			return new ResponseEntity<>(apiE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	//the built-in java exceptions
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> handleBadParameterTypeInURL(Exception ex) {
		ApiError apiE = new ApiError(400, "BAD REQUEST", "A parameter type in URL doesn't match the expected type");
		return new ResponseEntity<>(apiE, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleBadRequestBody(Exception ex) {
		ApiError apiE = new ApiError(400, "BAD REQUEST", "Something in your request body is not formatted properly");
		return new ResponseEntity<>(apiE, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class) 
	public ResponseEntity<ApiError> handleWrongRequestMethodSelected(Exception ex, HttpServletRequest request) {
		ApiError apiE = new ApiError(405, "METHOD NOT ALLOWED", request.getMethod() + " request doesn't work for the path " + request.getRequestURI());
		return new ResponseEntity<>(apiE, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiError> handleEntireApi404(Exception ex) {
		ApiError apiE = new ApiError(404, "NOT FOUND", "The entered path doesn't match any known paths for this API");
		return new ResponseEntity<>(apiE, HttpStatus.NOT_FOUND);
	}
		
	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ApiError> handleDBProblem(Exception ex) {
		ApiError apiE = new ApiError(500, "INTERNAL_SERVER_ERROR", "Something went wrong with the database");
		return new ResponseEntity<>(apiE, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}