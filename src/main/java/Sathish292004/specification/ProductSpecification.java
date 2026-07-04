package Sathish292004.specification;

import Sathish292004.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasCategory(String category) {

        return (root, query, criteriaBuilder) ->

                category == null || category.isBlank()
                        ? null
                        : criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Product> hasBrand(String brand) {

        return (root, query, criteriaBuilder) ->

                brand == null || brand.isBlank()
                        ? null
                        : criteriaBuilder.equal(root.get("brand"), brand);
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {

        return (root, query, criteriaBuilder) ->

                minPrice == null
                        ? null
                        : criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        minPrice
                );
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) ->

                maxPrice == null
                        ? null
                        : criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        maxPrice
                );
    }

    public static Specification<Product> availableOnly(Boolean available) {

        return (root, query, criteriaBuilder) ->

                available == null
                        ? null
                        : criteriaBuilder.equal(
                        root.get("productAvailable"),
                        available
                );
    }
}