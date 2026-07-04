package Sathish292004.repository;

import Sathish292004.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

}