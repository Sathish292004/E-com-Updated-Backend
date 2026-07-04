package Sathish292004.repository;

import Sathish292004.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>,
        JpaSpecificationExecutor<Product> {

    List<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrCategoryContainingIgnoreCase(
            String name,
            String brand,
            String category
    );
}
