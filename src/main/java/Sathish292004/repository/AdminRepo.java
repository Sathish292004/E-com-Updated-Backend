package Sathish292004.repository;

import Sathish292004.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Integer> {

    Optional<Admin> findByEmail(String email);

}