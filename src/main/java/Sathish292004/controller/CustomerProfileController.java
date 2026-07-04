package Sathish292004.controller;

import Sathish292004.dto.request.ChangePasswordRequest;
import Sathish292004.dto.response.CustomerProfileResponse;
import Sathish292004.dto.request.CustomerProfileUpdateRequest;
import Sathish292004.dto.response.OrderResponse;
import Sathish292004.service.CustomerService;
import Sathish292004.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Customer", description = "Customer Profile APIs")
@RequestMapping("/api/customer")
@CrossOrigin
public class CustomerProfileController {

    private final CustomerService customerService;
    private final OrderService orderService;

    public CustomerProfileController(CustomerService customerService,
                                     OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileResponse> getProfile(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                customerService.getProfile(email)
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody CustomerProfileUpdateRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                customerService.updateProfile(email, request)
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                customerService.changePassword(email, request)
        );
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getCustomerOrders(email)
        );
    }
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getCustomerOrder(
            Authentication authentication,
            @PathVariable String orderId) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getCustomerOrder(email, orderId)
        );
    }
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            Authentication authentication,
            @PathVariable String orderId) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.cancelOrder(email, orderId)
        );
    }
}