package com.tpt.tpt_ecom.controller;

import com.tpt.tpt_ecom.dto.AddressDTO;
import com.tpt.tpt_ecom.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/users")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(
                this.addressService.addNewAddress(addressDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/all/users")
    public ResponseEntity<Set<AddressDTO>> getAddressByUser() {
        return new ResponseEntity<>(
                this.addressService.getAddressesByUser(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{addressId}/users")
    public ResponseEntity<AddressDTO> getSpecificAddressByUser(@PathVariable Long addressId) {
        return new ResponseEntity<>(
                this.addressService.getSpecificAddressByUser(addressId),
                HttpStatus.OK
        );
    }

    @PutMapping("/{addressId}/users")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(
                this.addressService.updateAddress(addressId, addressDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{addressId}/users")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        return new ResponseEntity<>(
                this.addressService.deleteAddress(addressId),
                HttpStatus.OK
        );
    }
}
