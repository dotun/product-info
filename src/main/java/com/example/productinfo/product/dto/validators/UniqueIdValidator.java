package com.example.productinfo.product.dto.validators;

import com.example.productinfo.product.ProductRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueIdValidator implements ConstraintValidator<UniqueId, String> {

	private final ProductRepository productRepository;

	public UniqueIdValidator(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public void initialize(UniqueId constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !this.productRepository.existsById(value);
	}
}
