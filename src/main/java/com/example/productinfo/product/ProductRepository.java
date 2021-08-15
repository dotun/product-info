package com.example.productinfo.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, String> {

	List<ProductModel> getProductsByVendor(String vendor);
}
