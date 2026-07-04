package Sathish292004.dto.request;

public record ChangePasswordRequest(

        String currentPassword,
        String newPassword

) {
}