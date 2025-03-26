package Pavan.com.E_commerce_Application.Service;

import Pavan.com.E_commerce_Application.dto.AddressDTO;
import Pavan.com.E_commerce_Application.model.Address;
import Pavan.com.E_commerce_Application.model.User;
import Pavan.com.E_commerce_Application.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<Address> getAddressesByUser(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    @Transactional
    public Address createAddress(Long userId, AddressDTO addressDTO) {
        User user = userService.getUserById(userId);
        Address address = new Address();
        
        address.setUser(user);
        address.setStreetAddress(addressDTO.getStreetAddress());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        address.setPhoneNumber(addressDTO.getPhoneNumber());
        address.setDefault(addressDTO.isDefault());
        
        // If this is the first address or marked as default, set it as default
        List<Address> userAddresses = getAddressesByUser(userId);
        if (userAddresses.isEmpty() || address.isDefault()) {
            setDefaultAddress(userId, address);
        }

        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(Long id, AddressDTO addressDTO) {
        Address address = getAddressById(id);
        
        address.setStreetAddress(addressDTO.getStreetAddress());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        if (addressDTO.isDefault()) {
            setDefaultAddress(address.getUser().getId(), address);
        }

        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }

    @Transactional
    public Address setDefaultAddress(Long userId, Address address) {
        // Remove default status from all other addresses
        List<Address> userAddresses = getAddressesByUser(userId);
        userAddresses.forEach(a -> a.setDefault(false));
        addressRepository.saveAll(userAddresses);

        // Set the new default address
        address.setDefault(true);
        return addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public Address getDefaultAddress(Long userId) {
        List<Address> defaultAddresses = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        return defaultAddresses.isEmpty() ? null : defaultAddresses.get(0);
    }

    @Transactional(readOnly = true)
    public boolean isUserOwner(Long userId, String userEmail) {
        User user = userService.getUserById(userId);
        return user.getEmail().equals(userEmail);
    }

    @Transactional(readOnly = true)
    public boolean isAddressOwner(Long addressId, String userEmail) {
        Address address = getAddressById(addressId);
        return address.getUser().getEmail().equals(userEmail);
    }
} 