package Sathish292004.controller;

import Sathish292004.dto.request.AddressRequest;
import Sathish292004.dto.response.AddressResponse;
import Sathish292004.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/addresses")
@CrossOrigin
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            Authentication authentication,
            @RequestBody AddressRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.addAddress(email, request)
        );
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.getAddresses(email)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            Authentication authentication,
            @PathVariable Integer id,
            @RequestBody AddressRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.updateAddress(email, id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(
            Authentication authentication,
            @PathVariable Integer id) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.deleteAddress(email, id)
        );
    }
}