package Sathish292004.service;

import Sathish292004.model.Product;
import Sathish292004.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import Sathish292004.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import Sathish292004.dto.response.ProductPageResponse;
import java.math.BigDecimal;

import Sathish292004.exception.ProductNotFoundException;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(new Product(-1));
    }

    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());

        return productRepo.save(product);
    }

    public void deleteProduct(int id) {
        productRepo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
    return productRepo
            .findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    keyword,
                    keyword,
                    keyword
            );

}

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setBrand(product.getBrand());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        existing.setReleaseDate(product.getReleaseDate());
        existing.setProductAvailable(product.isProductAvailable());
        existing.setStockQuantity(product.getStockQuantity());

        if (imageFile != null && !imageFile.isEmpty()) {
            existing.setImageName(imageFile.getOriginalFilename());
            existing.setImageType(imageFile.getContentType());
            existing.setImageData(imageFile.getBytes());
        }

        return productRepo.save(existing);
    }
    public ProductPageResponse filterProducts(
            String category,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean available,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> specification = Specification
                .where(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasBrand(brand))
                .and(ProductSpecification.minPrice(minPrice))
                .and(ProductSpecification.maxPrice(maxPrice))
                .and(ProductSpecification.availableOnly(available));

        Page<Product> products = productRepo.findAll(specification, pageable);

        return new ProductPageResponse(
                products.getContent(),
                products.getNumber(),
                products.getTotalPages(),
                products.getTotalElements(),
                products.isLast()
        );
    }

}
