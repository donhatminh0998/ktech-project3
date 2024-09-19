package com.example.ktech_project_3.shop.repo;

import com.example.ktech_project_3.ItemCategory;
import com.example.ktech_project_3.shop.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
    @Query("SELECT s FROM ShopEntity s LEFT JOIN s.orders o " +
            "GROUP BY s.id " +
            "ORDER BY MAX(o.orderTime) DESC")
    List<ShopEntity> findAllShopsOrderedByLatestOrder();


    @Query("SELECT s FROM ShopEntity s LEFT JOIN s.orders o " +
            "WHERE s.shopName LIKE %:shopName% " +
            "GROUP BY s.id " +
            "ORDER BY MAX(o.orderTime) DESC")
    List<ShopEntity> findShopsByNameOrderedByLatestOrder(
            @Param("shopName") String shopName);


    @Query("SELECT s FROM ShopEntity s LEFT JOIN s.orders o " +
            "WHERE s.category = :category " +
            "GROUP BY s.id " +
            "ORDER BY MAX(o.orderTime) DESC")
    List<ShopEntity> findShopsByCategoryOrderedByLatestOrder(
            @Param("category") ItemCategory category);


    @Query("SELECT s FROM ShopEntity s LEFT JOIN s.orders o " +
            "WHERE (:name IS NULL OR s.shopName LIKE %:name%) " +
            "AND (:category IS NULL OR s.category = :category) " +
            "GROUP BY s.id " +
            "ORDER BY MAX(o.orderTime) DESC")
    List<ShopEntity> findShopsByNameAndCategoryOrderedByLatestOrder(
            @Param("name") String name,
            @Param("category") ItemCategory category);
}
