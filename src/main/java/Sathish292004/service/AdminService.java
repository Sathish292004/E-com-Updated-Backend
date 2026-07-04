package Sathish292004.service;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import Sathish292004.dto.response.AdminDashboardResponse;
import Sathish292004.dto.response.AdminProfileResponse;
import Sathish292004.model.Admin;
import Sathish292004.model.Order;
import Sathish292004.model.OrderItem;
import Sathish292004.repository.AdminRepo;
import Sathish292004.repository.CategoryRepo;
import Sathish292004.repository.CustomerRepo;
import Sathish292004.repository.OrderRepo;
import Sathish292004.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Sathish292004.dto.response.AdminCustomerResponse;
import Sathish292004.model.Customer;
import java.util.ArrayList;
import Sathish292004.dto.response.AdminOrderResponse;
import Sathish292004.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderRepo orderRepo;

    public Optional<Admin> findByEmail(String email) {
        return adminRepo.findByEmail(email);
    }

    public Admin save(Admin admin) {
        return adminRepo.save(admin);
    }

    public AdminDashboardResponse getDashboard() {

        long totalProducts = productRepo.count();
        long totalCategories = categoryRepo.count();
        long totalCustomers = customerRepo.count();
        long totalOrders = orderRepo.count();

        BigDecimal totalRevenue = BigDecimal.ZERO;

        List<Order> orders = orderRepo.findAll();

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                totalRevenue = totalRevenue.add(item.getTotalPrice());
            }
        }

        return new AdminDashboardResponse(
                totalProducts,
                totalCategories,
                totalCustomers,
                totalOrders,
                totalRevenue
        );
    }


    public List<AdminCustomerResponse> getAllCustomers() {

        List<Customer> customers = customerRepo.findAll();

        List<AdminCustomerResponse> response = new ArrayList<>();

        for (Customer customer : customers) {

            List<Order> orders = orderRepo.findByEmail(customer.getEmail());

            int totalOrders = orders.size();

            BigDecimal totalSpent = BigDecimal.ZERO;

            LocalDate since = null;

            if (!orders.isEmpty()) {
                since = orders.get(0).getOrderDate();
            }

            for (Order order : orders) {
                for (OrderItem item : order.getOrderItems()) {
                    totalSpent = totalSpent.add(item.getTotalPrice());
                }
            }

            response.add(
                    new AdminCustomerResponse(
                            customer.getId(),
                            customer.getName(),
                            customer.getEmail(),
                            customer.getPhone(),
                            customer.getAddress(),
                            customer.getRole(),
                            totalOrders,
                            totalSpent,
                            since
                    )
            );
        }

        return response;
    }

    public List<AdminOrderResponse> getAllOrders() {

        List<Order> orders = orderRepo.findAll();

        List<AdminOrderResponse> response = new ArrayList<>();

        for (Order order : orders) {

            BigDecimal total = BigDecimal.ZERO;

            for (OrderItem item : order.getOrderItems()) {
                total = total.add(item.getTotalPrice());
            }

            response.add(
                    new AdminOrderResponse(
                            order.getOrderId(),
                            order.getCustomerName(),
                            order.getEmail(),
                            order.getStatus(),
                            order.getOrderDate(),
                            total,
                            order.getOrderItems().size()
                    )
            );
        }

        return response;
    }
//    public AdminProfileResponse getProfile(String email) {
//
//        Customer admin = customerRepo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Admin not found"));
//
//        return new AdminProfileResponse(
//                admin.getName(),
//                admin.getEmail(),
//                admin.getRole()
//        );
//    }

    public AdminProfileResponse getProfile(String email) {

        Admin admin = adminRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return new AdminProfileResponse(
                admin.getName(),
                admin.getEmail(),
                admin.getRole()
        );
    }
}