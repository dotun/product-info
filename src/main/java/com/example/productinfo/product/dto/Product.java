package com.example.productinfo.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Product implements Serializable {

	private static final long serialVersionUID = 560721268301983444L;
	@NotBlank(message = "{product.name.mandatory}")
	@Size(max = 10, message = "{product.name.tooLong}")
	private final String name;

	@NotBlank(message = "{vendor.name.mandatory}")
	@Size(max = 10, message = "{vendor.name.tooLong}")
	private final String vendor;

	@NotNull(message = "{product.price.mandatory}")
	@Min(value = 0, message = "{price.not.negative}")
	private final Double price;

	@Future
	@JsonFormat(pattern = "dd-MM-yyyy")
	private final LocalDate expirationDate;

	public Product(String name, String vendor, Double price, LocalDate expirationDate) {
		this.name = name;
		this.vendor = vendor;
		this.price = price;
		this.expirationDate = expirationDate;
	}

	public String getName() {
		return name;
	}

	public String getVendor() {
		return vendor;
	}

	public Double getPrice() {
		return price;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}
}
