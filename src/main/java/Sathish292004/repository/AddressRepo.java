package Sathish292004.repository;

import Sathish292004.model.Address;
import Sathish292004.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Integer> {

    List<Address> findByCustomer(Customer customer);

    Optional<Address> findByIdAndCustomer(Integer id, Customer customer);
}