package Pavan.com.E_commerce_Application.Service;
import Pavan.com.E_commerce_Application.model.User;
import Pavan.com.E_commerce_Application.repository.UserRepository;
import Pavan.com.E_commerce_Application.dto.UserDTO;
import Pavan.com.E_commerce_Application.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        
        // If no roles are provided, set default role as USER
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Set<String> defaultRoles = new HashSet<>();
            defaultRoles.add("ROLE_USER");
            user.setRoles(defaultRoles);
        } else {
            // Ensure all roles start with "ROLE_"
            Set<String> formattedRoles = new HashSet<>();
            for (String role : request.getRoles()) {
                if (!role.startsWith("ROLE_")) {
                    formattedRoles.add("ROLE_" + role.toUpperCase());
                } else {
                    formattedRoles.add(role);
                }
            }
            user.setRoles(formattedRoles);
        }
        
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getName());
        user.setLastName(userDTO.getName()); // Assuming name is full name
        user.setPhoneNumber(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getRole() != null) {
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_" + userDTO.getRole().toUpperCase());
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Transactional
    public User updateRoles(Long userId, Set<String> newRoles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Format roles to ensure they start with "ROLE_"
        Set<String> formattedRoles = new HashSet<>();
        for (String role : newRoles) {
            if (!role.startsWith("ROLE_")) {
                formattedRoles.add("ROLE_" + role.toUpperCase());
            } else {
                formattedRoles.add(role);
            }
        }
        user.setRoles(formattedRoles);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRolesContaining(role);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getFirstName() + " " + user.getLastName());
        dto.setPhone(user.getPhoneNumber());
        if (!user.getRoles().isEmpty()) {
            dto.setRole(user.getRoles().iterator().next().replace("ROLE_", ""));
        }
        return dto;
    }
}
