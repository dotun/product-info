package com.example.productinfo.product;

import com.example.productinfo.product.dto.Product;
import com.example.productinfo.product.dto.validators.UniqueId;
import com.example.productinfo.product.exceptionhandlers.ConstraintViolationsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.example.productinfo.product.utils.ApiDocumentationUtils.PRODUCT_BODY_EXAMPLE;
import static com.example.productinfo.product.utils.ApiDocumentationUtils.VENDOR_PRODUCTS_EXAMPLE;

@ApiResponse(
	responseCode = "400",
	description = "One or more request parameters are not valid",
	content = {
		@Content(
			mediaType = MediaType.APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = ConstraintViolationsHandler.ErrorWrapper.class)
		)
	}
)
@Validated
@RestController
@RequestMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductInfoController {

	private final ProductService productService;

	public ProductInfoController(ProductService productService) {
		this.productService = productService;
	}

	@Operation(summary = "Create a Product",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				examples = @ExampleObject(name = "create product", value = PRODUCT_BODY_EXAMPLE)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Product created"
			),

		}
	)
	@PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createProduct(@PathVariable("id") @Valid @UniqueId String id, @RequestBody @Valid Product product) {
		this.productService.createProduct(id, product);
	}

	@Operation(
		summary = "Get products by vendor",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Product list for given vendor",
				content = {
					@Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						array = @ArraySchema(schema = @Schema(implementation = Product.class)),
						examples = @ExampleObject(
							name = "vendor products",
							value = VENDOR_PRODUCTS_EXAMPLE,
							description = "A list of products for the given vendor"
						)
					)
				}
			)
		}
	)
	@GetMapping
	public ResponseEntity<Collection<Product>> getProductsByVendor(@RequestParam("vendor") @NotBlank(message = "{vendor.name.mandatory}") String vendor) {
		List<Product> products = productService.getProductsByVendor(vendor);

		return ResponseEntity.ok(products);
	}

	@Operation(
		summary = "Get product by id",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Returns a single product for id if found",
				content = {
					@Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						examples = @ExampleObject(
							name = "product",
							value = PRODUCT_BODY_EXAMPLE
						)
					)
				}
			),
			@ApiResponse(
				responseCode = "404",
				description = "Product with given id not found",
				content = {
					@Content(
						schema = @Schema(implementation = Void.class)
					)
				}
			)
		}
	)
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") @NotBlank String id) {
		Optional<Product> productDto = productService.getProductById(id);

		return productDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
