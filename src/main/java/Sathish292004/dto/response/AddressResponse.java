package Sathish292004.dto.response;

public record AddressResponse(

        Integer id,
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