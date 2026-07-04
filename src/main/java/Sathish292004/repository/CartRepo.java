package Sathish292004.repository;

import Sathish292004.model.Cart;
import Sathish292004.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByCustomer(Customer customer);

}