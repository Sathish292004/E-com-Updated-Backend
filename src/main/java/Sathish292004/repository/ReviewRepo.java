package Sathish292004.repository;

import Sathish292004.model.Customer;
import Sathish292004.model.Product;
import Sathish292004.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepo extends JpaRepository<Review, Integer> {

    List<Review> findByProduct(Product product);

    Optional<Review> findByCustomerAndProduct(Customer customer,
                                              Product product);

    Optional<Review> findByIdAndCustomer(Integer id,
                                         Customer customer);
}