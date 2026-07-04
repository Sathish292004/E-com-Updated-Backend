package Sathish292004.controller;

import Sathish292004.dto.response.*;
import Sathish292004.service.AdminService;
import Sathish292004.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.core.Authentication;



@RestController
@Tag(name = "Admin", description = "Admin Management APIs")
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final AdminService adminService;
    private final ReviewService reviewService;

    public AdminController(AdminService adminService,
                           ReviewService reviewService) {
        this.adminService = adminService;
        this.reviewService = reviewService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard()
        );
    }
//
//    @GetMapping("/customers")
//    public ResponseEntity<List<AdminCustomerResponse>> getAllCustomers() {
//
//        return ResponseEntity.ok(
//                adminService.getAllCustomers()
//        );
//    }

    @GetMapping("/customers")
    public ResponseEntity<List<AdminCustomerResponse>> getAllCustomers(
            Authentication authentication) {

        System.out.println("==================================");
        System.out.println("User: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("==================================");

        System.out.println("Before service");

        List<AdminCustomerResponse> customers = adminService.getAllCustomers();

        System.out.println("After service");

        return ResponseEntity.ok(customers);
    }


    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderResponse>> getAllOrders(Authentication authentication) {

        System.out.println("========== ADMIN ORDERS ==========");
        System.out.println("USER: " + authentication.getName());
        System.out.println("ROLE: " + authentication.getAuthorities());

        return ResponseEntity.ok(
                adminService.getAllOrders()
        );
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<AdminReviewResponse>> getAllReviews() {

        return ResponseEntity.ok(
                reviewService.getAllReviews()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<AdminProfileResponse> getProfile(Authentication authentication) {

        return ResponseEntity.ok(
                adminService.getProfile(authentication.getName())
        );
    }
}