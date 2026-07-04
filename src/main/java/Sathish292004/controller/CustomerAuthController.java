package Sathish292004.controller;

import Sathish292004.dto.response.CustomerAuthResponse;
import Sathish292004.dto.request.CustomerLoginRequest;
import Sathish292004.dto.request.CustomerRegisterRequest;
import Sathish292004.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class CustomerAuthController {

    private final CustomerService customerService;

    public CustomerAuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody CustomerRegisterRequest request) {

        return new ResponseEntity<>(
                customerService.register(request),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerAuthResponse> login(
            @Valid @RequestBody CustomerLoginRequest request) {

        return ResponseEntity.ok(
                customerService.login(request)
        );
    }
}