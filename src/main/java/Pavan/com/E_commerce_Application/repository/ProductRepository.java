package Pavan.com.E_commerce_Application.repository;

import Pavan.com.E_commerce_Application.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByNameContaining(String keyword);
}