package com.example.ktech_project_3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j

public class EnumController {
    @GetMapping("/itemCategory")
    public ResponseEntity<List<String>> getCategories() {
        // Chuyển đổi tất cả các giá trị enum thành danh sách chuỗi
        List<String> categories = Arrays.stream(ItemCategory.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);}
}