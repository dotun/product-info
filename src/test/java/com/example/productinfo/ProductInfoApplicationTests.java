package com.example.productinfo;

import com.example.productinfo.product.ProductService;
import com.example.productinfo.product.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductInfoApplicationTests {

	@MockBean
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateProductStatusOk() throws Exception {
		Product product = new Product("product_1", "vendor_1", 2000.0, LocalDate.now().plusDays(1));
		postProduct("id_1", product)
			.andExpect(status().isOk());
	}

	@Test
	void testCreateProductWithNegativePriceStatusBadRequest() throws Exception {
		Product product = new Product("product_1", "vendor_1_for_product_1", -2000.0, LocalDate.now().plusDays(1));
		postProduct("id_2", product)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors", containsInAnyOrder("Price cannot be negative", "Vendor name is too long")));
	}

	@Test
	void testProductsGetByVendor() throws Exception {
		Product productDto1 = new Product("product_1", "vendor_1", 2000.0, LocalDate.now().plusDays(1));
		Product productDto2 = new Product("product_2", "vendor_1", 2000.0, LocalDate.now().plusDays(1));
		Product productDto3 = new Product("product_2", "vendor_2", 2000.0, LocalDate.now().plusDays(1));

		final String vendor = "vendor_1";

		List<Product> products = Arrays.asList(productDto1, productDto2, productDto3);
		when(productService.getProductsByVendor(anyString())).thenReturn(products.stream().filter(product -> product.getVendor().equals(vendor)).collect(Collectors.toList()));

		mockMvc.perform(
			get("/products")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.param("vendor", "vendor_1")
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$..vendor", everyItem(is(vendor))));
	}

	private ResultActions postProduct(String id, Product product) throws Exception {
		return mockMvc.perform(
			post("/products/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(product))
		);
	}
}
