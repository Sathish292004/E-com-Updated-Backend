package Sathish292004.security;

import Sathish292004.model.Customer;
import Sathish292004.repository.CustomerRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CustomerRepo customerRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public OAuth2AuthenticationSuccessHandler(CustomerRepo customerRepo,
                                              JwtService jwtService,
                                              PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        Customer customer = customerRepo.findByEmail(email).orElse(null);

        if (customer == null) {

            customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setRole("CUSTOMER");

            customer.setPassword(
                    passwordEncoder.encode(UUID.randomUUID().toString())
            );

            customerRepo.save(customer);
        }

        UserDetails userDetails = User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .roles("CUSTOMER")
                .build();

        String token = jwtService.generateToken(userDetails);

        response.sendRedirect(
                "http://localhost:8081/oauth2/success?token=" + token
        );
    }
}
