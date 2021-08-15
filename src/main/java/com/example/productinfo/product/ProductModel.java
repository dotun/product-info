package com.example.productinfo.product;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Table(name = "products")
@Entity(name = "Product")
public class ProductModel implements Serializable {
	private static final long serialVersionUID = 3114174275726385951L;

	@JsonIgnore
	@Id
	private String id;

	private String vendor;

	private String name;

	private Double price;

	private LocalDate expirationDate;

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductModel productModel = (ProductModel) o;
		return id.equals(productModel.id) && vendor.equals(productModel.vendor) && name.equals(productModel.name) && price.equals(productModel.price) && Objects.equals(expirationDate, productModel.expirationDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, vendor, name, price, expirationDate);
	}
}
