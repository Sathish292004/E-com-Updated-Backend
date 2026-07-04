package Sathish292004.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdminCustomerResponse(
        int id,
        String name,
        String email,
        String phone,
        String address,
        String role,
        int orders,
        BigDecimal spent,
        LocalDate since
) {}