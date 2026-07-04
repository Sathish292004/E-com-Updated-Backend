package Sathish292004.dto.request;

public record AddressRequest(

        String fullName,
        String phone,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postalCode,
        String country,
        boolean defaultAddress

) {
}