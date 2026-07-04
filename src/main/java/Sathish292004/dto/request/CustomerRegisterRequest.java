package Sathish292004.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRegisterRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Enter a valid 10-digit Indian mobile number"
        )
        String phone,

        @NotBlank(message = "Address is required")
        @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
        String address,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password

) {
}