package com.example.ktech_project_3.shop.repo;

import com.example.ktech_project_3.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsById(Long productId);

    @Query("SELECT p FROM Product p " +
            "JOIN p.shop s " +
            "WHERE p.nameItem LIKE %:name% " +
            "AND p.price BETWEEN :minPrice AND :maxPrice " +
            "ORDER BY p.price")
    List<Product> findProductsByShopAndNameAndPriceRange(
            @Param("name") String name,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice);



    @Query("SELECT p FROM Product p " +
            "JOIN p.shop s " +
            "WHERE p.price BETWEEN :minPrice AND :maxPrice " +
            "ORDER BY p.price")
    List<Product> findProductsByShopAndPriceRange(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice);

}