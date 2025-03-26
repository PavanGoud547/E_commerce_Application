package Pavan.com.E_commerce_Application.repository;

import Pavan.com.E_commerce_Application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r LIKE %:role%")
    List<User> findByRolesContaining(@Param("role") String role);
}

