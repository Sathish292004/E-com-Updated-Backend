package Sathish292004.dto.response;

import java.math.BigDecimal;

public record AdminDashboardResponse(

        long totalProducts,
        long totalCategories,
        long totalCustomers,
        long totalOrders,
        BigDecimal totalRevenue

) {
}
