package Sathish292004.service;

import Sathish292004.dto.request.AddressRequest;
import Sathish292004.dto.response.AddressResponse;
import Sathish292004.exception.CustomerNotFoundException;
import Sathish292004.model.Address;
import Sathish292004.model.Customer;
import Sathish292004.repository.AddressRepo;
import Sathish292004.repository.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepo addressRepo;
    private final CustomerRepo customerRepo;

    public AddressService(AddressRepo addressRepo,
                          CustomerRepo customerRepo) {
        this.addressRepo = addressRepo;
        this.customerRepo = customerRepo;
    }

    public AddressResponse addAddress(String email, AddressRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (request.defaultAddress()) {

            List<Address> addresses = addressRepo.findByCustomer(customer);

            for (Address address : addresses) {
                address.setDefaultAddress(false);
                addressRepo.save(address);
            }
        }

        Address address = Address.builder()
                .fullName(request.fullName())
                .phone(request.phone())
                .addressLine1(request.addressLine1())
                .addressLine2(request.addressLine2())
                .city(request.city())
                .state(request.state())
                .postalCode(request.postalCode())
                .country(request.country())
                .defaultAddress(request.defaultAddress())
                .customer(customer)
                .build();

        Address savedAddress = addressRepo.save(address);

        return new AddressResponse(
                savedAddress.getId(),
                savedAddress.getFullName(),
                savedAddress.getPhone(),
                savedAddress.getAddressLine1(),
                savedAddress.getAddressLine2(),
                savedAddress.getCity(),
                savedAddress.getState(),
                savedAddress.getPostalCode(),
                savedAddress.getCountry(),
                savedAddress.isDefaultAddress()
        );
    }

    public List<AddressResponse> getAddresses(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Address> addresses = addressRepo.findByCustomer(customer);

        List<AddressResponse> response = new ArrayList<>();

        for (Address address : addresses) {

            response.add(
                    new AddressResponse(
                            address.getId(),
                            address.getFullName(),
                            address.getPhone(),
                            address.getAddressLine1(),
                            address.getAddressLine2(),
                            address.getCity(),
                            address.getState(),
                            address.getPostalCode(),
                            address.getCountry(),
                            address.isDefaultAddress()
                    )
            );
        }

        return response;
    }

    public AddressResponse updateAddress(String email,
                                         Integer id,
                                         AddressRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Address address = addressRepo.findByIdAndCustomer(id, customer)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (request.defaultAddress()) {

            List<Address> addresses = addressRepo.findByCustomer(customer);

            for (Address addr : addresses) {
                addr.setDefaultAddress(false);
                addressRepo.save(addr);
            }
        }

        address.setFullName(request.fullName());
        address.setPhone(request.phone());
        address.setAddressLine1(request.addressLine1());
        address.setAddressLine2(request.addressLine2());
        address.setCity(request.city());
        address.setState(request.state());
        address.setPostalCode(request.postalCode());
        address.setCountry(request.country());
        address.setDefaultAddress(request.defaultAddress());

        Address updated = addressRepo.save(address);

        return new AddressResponse(
                updated.getId(),
                updated.getFullName(),
                updated.getPhone(),
                updated.getAddressLine1(),
                updated.getAddressLine2(),
                updated.getCity(),
                updated.getState(),
                updated.getPostalCode(),
                updated.getCountry(),
                updated.isDefaultAddress()
        );
    }

    public String deleteAddress(String email, Integer id) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Address address = addressRepo.findByIdAndCustomer(id, customer)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepo.delete(address);

        return "Address deleted successfully";
    }
}