package Sathish292004.dto.response;

import java.math.BigDecimal;

public record WishlistResponse(

        Integer productId,
        String productName,
        String brand,
        String category,
        BigDecimal price,
        String imageName,
        boolean productAvailable

) {
}