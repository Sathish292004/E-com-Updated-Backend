package Sathish292004.dto.response;

import java.time.LocalDate;

public record ReviewResponse(

        Integer id,
        String customerName,
        String productName,
        Integer rating,
        String comment,
        LocalDate reviewDate
) {
}