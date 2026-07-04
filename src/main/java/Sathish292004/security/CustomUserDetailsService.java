package Sathish292004.security;

import Sathish292004.model.Admin;
import Sathish292004.model.Customer;
import Sathish292004.repository.AdminRepo;
import Sathish292004.repository.CustomerRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepo adminRepo;
    private final CustomerRepo customerRepo;

    public CustomUserDetailsService(AdminRepo adminRepo,
                                    CustomerRepo customerRepo) {
        this.adminRepo = adminRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Check Admin first
        Admin admin = adminRepo.findByEmail(email).orElse(null);

        if (admin != null) {
            return User.builder()
                    .username(admin.getEmail())
                    .password(admin.getPassword())
                    .roles(admin.getRole())
                    .build();
        }

        // Check Customer
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .roles(customer.getRole())
                .build();
    }
}