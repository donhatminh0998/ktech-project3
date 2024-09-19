package com.example.ktech_project_3.request.repo;

import com.example.ktech_project_3.request.entity.OpenRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpenRequestRepository extends JpaRepository<OpenRequest, Long> {
    @Query("SELECT r FROM OpenRequest r ORDER BY CASE r.requestStatus " +
            "WHEN 'PENDING' THEN 1 " +
            "WHEN 'ACCEPTED' THEN 2 " +
            "WHEN 'REJECTED' THEN 3 " +
            "ELSE 4 END, r.createdAt DESC")
    List<OpenRequest> findAllOrderedByStatusAndCreatedAt();

    boolean existsByRequestStatusAndUserId(String status, Long userId);
}