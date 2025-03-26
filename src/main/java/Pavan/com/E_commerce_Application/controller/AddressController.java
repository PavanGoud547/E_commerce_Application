package Pavan.com.E_commerce_Application.controller;

import Pavan.com.E_commerce_Application.dto.AddressDTO;
import Pavan.com.E_commerce_Application.model.Address;
import Pavan.com.E_commerce_Application.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@PreAuthorize("hasRole('USER')")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Address>> getAddressesByUser(@PathVariable Long userId) {
        validateUserAccess(userId);
        return ResponseEntity.ok(addressService.getAddressesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        validateAddressAccess(id);
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Address> createAddress(
            @PathVariable Long userId,
            @Valid @RequestBody AddressDTO addressDTO) {
        validateUserAccess(userId);
        return ResponseEntity.ok(addressService.createAddress(userId, addressDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressDTO addressDTO) {
        validateAddressAccess(id);
        return ResponseEntity.ok(addressService.updateAddress(id, addressDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        validateAddressAccess(id);
        addressService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userId}/default")
    public ResponseEntity<Address> setDefaultAddress(
            @PathVariable Long userId,
            @RequestBody AddressDTO addressDTO) {
        validateUserAccess(userId);
        Address address = addressService.createAddress(userId, addressDTO);
        return ResponseEntity.ok(addressService.setDefaultAddress(userId, address));
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<Address> getDefaultAddress(@PathVariable Long userId) {
        validateUserAccess(userId);
        return ResponseEntity.ok(addressService.getDefaultAddress(userId));
    }

    private void validateUserAccess(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        if (!addressService.isUserOwner(userId, currentUserEmail)) {
            throw new RuntimeException("Access denied");
        }
    }

    private void validateAddressAccess(Long addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        if (!addressService.isAddressOwner(addressId, currentUserEmail)) {
            throw new RuntimeException("Access denied");
        }
    }
} 