package Sathish292004.service;

import Sathish292004.dto.response.CustomerProfileResponse;
import Sathish292004.dto.request.CustomerProfileUpdateRequest;
import Sathish292004.security.JwtService;
import Sathish292004.dto.response.CustomerAuthResponse;
import Sathish292004.dto.request.CustomerLoginRequest;
import Sathish292004.dto.request.CustomerRegisterRequest;
import Sathish292004.exception.CustomerNotFoundException;
import Sathish292004.model.Customer;
import Sathish292004.repository.CustomerRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Sathish292004.dto.request.ChangePasswordRequest;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public CustomerService(CustomerRepo customerRepo,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public CustomerAuthResponse register(CustomerRegisterRequest request) {

        if (customerRepo.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        Customer customer = new Customer();

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setAddress(request.address());
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setRole("CUSTOMER");

        customerRepo.save(customer);

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(customer.getEmail())
                        .password(customer.getPassword())
                        .roles(customer.getRole())
                        .build()
        );

        return new CustomerAuthResponse(
                token,
                customer.getName(),
                customer.getEmail(),
                customer.getRole()
        );
    }

    public CustomerAuthResponse login(CustomerLoginRequest request) {

        Customer customer = customerRepo.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(request.password(), customer.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(customer.getEmail())
                        .password(customer.getPassword())
                        .roles(customer.getRole())
                        .build()
        );

        return new CustomerAuthResponse(
                token,
                customer.getName(),
                customer.getEmail(),
                customer.getRole()
        );

    }
    public CustomerProfileResponse getProfile(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return new CustomerProfileResponse(
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
        );
    }

    public CustomerProfileResponse updateProfile(
            String email,
            CustomerProfileUpdateRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(request.name());
        customer.setPhone(request.phone());
        customer.setAddress(request.address());

        customerRepo.save(customer);

        return new CustomerProfileResponse(
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
        );
    }
    public String changePassword(
            String email,
            ChangePasswordRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        if (!passwordEncoder.matches(
                request.currentPassword(),
                customer.getPassword())) {

            throw new RuntimeException("Current password is incorrect");
        }

        customer.setPassword(
                passwordEncoder.encode(request.newPassword())
        );

        customerRepo.save(customer);

        return "Password changed successfully";
    }
}