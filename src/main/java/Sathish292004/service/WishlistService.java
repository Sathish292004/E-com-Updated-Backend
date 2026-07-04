package Sathish292004.service;

import Sathish292004.dto.response.WishlistResponse;
import Sathish292004.exception.CustomerNotFoundException;
import Sathish292004.exception.ProductNotFoundException;
import Sathish292004.model.Customer;
import Sathish292004.model.Product;
import Sathish292004.model.Wishlist;
import Sathish292004.repository.CustomerRepo;
import Sathish292004.repository.ProductRepo;
import Sathish292004.repository.WishlistRepo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepo wishlistRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;

    public WishlistService(WishlistRepo wishlistRepo,
                           CustomerRepo customerRepo,
                           ProductRepo productRepo) {
        this.wishlistRepo = wishlistRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    public String addToWishlist(String email, Integer productId) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (wishlistRepo.findByCustomerAndProduct(customer, product).isPresent()) {
            throw new RuntimeException("Product already in wishlist");
        }

        Wishlist wishlist = Wishlist.builder()
                .customer(customer)
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();

        wishlistRepo.save(wishlist);

        return "Product added to wishlist";
    }

    public List<WishlistResponse> getWishlist(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        List<Wishlist> wishlist = wishlistRepo.findByCustomer(customer);

        List<WishlistResponse> response = new ArrayList<>();

        for (Wishlist item : wishlist) {

            Product product = item.getProduct();

            response.add(
                    new WishlistResponse(
                            product.getId(),
                            product.getName(),
                            product.getBrand(),
                            product.getCategory(),
                            product.getPrice(),
                            product.getImageName(),
                            product.isProductAvailable()
                    )
            );
        }

        return response;
    }

    @Transactional
    public String removeFromWishlist(String email, Integer productId) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        wishlistRepo.deleteByCustomerAndProduct(customer, product);

        return "Product removed from wishlist";
    }
}