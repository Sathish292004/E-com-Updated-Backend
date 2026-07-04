package Sathish292004.dto.response;

public record ApiResponse<T>(

        boolean success,
        String message,
        T data

) {
}