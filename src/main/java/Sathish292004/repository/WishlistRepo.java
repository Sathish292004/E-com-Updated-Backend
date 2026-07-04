package Sathish292004.repository;

import Sathish292004.model.Customer;
import Sathish292004.model.Product;
import Sathish292004.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepo extends JpaRepository<Wishlist, Integer> {

    List<Wishlist> findByCustomer(Customer customer);

    Optional<Wishlist> findByCustomerAndProduct(Customer customer, Product product);

    void deleteByCustomerAndProduct(Customer customer, Product product);
}