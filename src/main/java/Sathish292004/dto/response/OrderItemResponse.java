package Sathish292004.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productName,
        int quantity,
        BigDecimal totalPrice

) {
}
