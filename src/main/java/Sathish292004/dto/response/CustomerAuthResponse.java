package Sathish292004.dto.response;

public record CustomerAuthResponse(

        String token,
        String name,
        String email,
        String role

) {
}