package Sathish292004.repository;

import Sathish292004.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    List<Category> findByNameContainingIgnoreCase(String keyword);

}