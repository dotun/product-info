package com.example.productinfo.product;

import com.example.productinfo.product.dto.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public void createProduct(String id, Product product) {
		ProductModel productModel = new ProductModel();
		productModel.setId(id);
		productModel.setName(product.getName());
		productModel.setVendor(product.getVendor());
		productModel.setPrice(product.getPrice());
		productModel.setExpirationDate(product.getExpirationDate());

		productRepository.save(productModel);
	}

	@Override
	public List<Product> getProductsByVendor(String vendor) {
		List<ProductModel> productModels = productRepository.getProductsByVendor(vendor);

		return productModels.stream().map(productModel ->
			new Product(productModel.getName(), productModel.getVendor(), productModel.getPrice(), productModel.getExpirationDate())
		).collect(Collectors.toList());
	}

	@Override
	public Optional<Product> getProductById(String id) {
		Optional<ProductModel> optionalProduct = productRepository.findById(id);

		return optionalProduct.map(productModel -> new Product(productModel.getName(), productModel.getVendor(), productModel.getPrice(), productModel.getExpirationDate()));
	}
}
