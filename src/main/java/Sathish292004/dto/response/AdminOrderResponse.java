package Sathish292004.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdminOrderResponse(

        String orderId,
        String customerName,
        String email,
        String status,
        LocalDate orderDate,
        BigDecimal totalAmount,
        int items

) {
}