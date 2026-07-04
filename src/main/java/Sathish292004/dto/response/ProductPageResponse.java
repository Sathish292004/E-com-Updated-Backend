package Sathish292004.dto.response;

import Sathish292004.model.Product;

import java.util.List;

public record ProductPageResponse(

        List<Product> products,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean last

) {
}