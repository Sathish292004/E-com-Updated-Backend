package Sathish292004.controller;

import Sathish292004.model.Product;
import Sathish292004.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import Sathish292004.dto.response.ProductPageResponse;
import java.math.BigDecimal;
import Sathish292004.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;



import java.io.IOException;
import java.util.Collections;
import java.util.List;


@RestController
@Tag(name = "Products", description = "Product Management APIs")
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product.getId() > 0) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product.getId() > 0) {
            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product savedProduct = null;
        try {
            savedProduct = productService.addOrUpdateProduct(product, imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestPart Product product,
            @RequestPart(required = false) MultipartFile imageFile) {

        try {
            Product updated = productService.updateProduct(id, product, imageFile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> getProductsByName(@RequestParam String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/filter")
    public ResponseEntity<ApiResponse<ProductPageResponse>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean available,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        ProductPageResponse response = productService.filterProducts(
                category,
                brand,
                minPrice,
                maxPrice,
                available,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Products fetched successfully",
                        response
                )
        );
    }

}
