package com.example.productinfo.product.exceptionhandlers;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ConstraintViolationsHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleConstraintViolation(ex, headers, status, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleConstraintViolations(ConstraintViolationException ex, WebRequest request) {
		return handleConstraintViolation(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	private ResponseEntity<Object> handleConstraintViolation(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<>();

		if (ex instanceof MethodArgumentNotValidException) {
			List<String> methodArgumentNotValidErrors = ((MethodArgumentNotValidException) ex).getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
			errors.addAll(methodArgumentNotValidErrors);
		} else if (ex instanceof ConstraintViolationException) {
			List<String> constraintViolationErrors = ((ConstraintViolationException) ex).getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
			errors.addAll(constraintViolationErrors);
		}

		return handleExceptionInternal(ex, new ErrorWrapper(errors), headers, status, request);
	}

	public static class ErrorWrapper {
		private final List<String> errors;

		public ErrorWrapper(List<String> errors) {
			this.errors = errors;
		}

		public List<String> getErrors() {
			return this.errors;
		}
	}
}
