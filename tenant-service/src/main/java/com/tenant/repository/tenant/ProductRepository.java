package com.tenant.repository.tenant;

import com.tenant.entity.tenant.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
