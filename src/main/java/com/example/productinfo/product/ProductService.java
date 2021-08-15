package com.example.productinfo.product;

import com.example.productinfo.product.dto.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {

	/**
	 * Create and save a product given the required parameters.
	 *
	 * @param id The given id of the product.
	 * @param product The data transfer object containing the parameters of the product.
	 */
	void createProduct(String id, Product product);

	/**
	 * Gets a list of products for a given vendor.
	 * @param vendor The name of the vendor whose products should be returned
	 * @return List of products for given vendor.
	 */
	List<Product> getProductsByVendor(String vendor);

	/**
	 * Fetch a single product by given id.
	 * @param id of product
	 * @return Optional Product if found.
	 */
	Optional<Product> getProductById(String id);

}
