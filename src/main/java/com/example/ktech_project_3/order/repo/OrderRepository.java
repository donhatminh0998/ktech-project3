package com.example.ktech_project_3.order.repo;

import com.example.ktech_project_3.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    @Query("SELECT o FROM OrderEntity o WHERE o.shop.id = :shopId ORDER BY " +
            "CASE WHEN o.status = 'PENDING' THEN 0 ELSE 1 END, o.orderTime DESC")
    List<OrderEntity> findOrdersByShopIdOrdered(@Param("shopId") Long shopId);


    @Query("SELECT o FROM OrderEntity o WHERE o.user.id = :userId ORDER BY o.orderTime DESC")
    List<OrderEntity> findOrdersByUserIdOrderedByTime(@Param("userId") Long userId);


}
