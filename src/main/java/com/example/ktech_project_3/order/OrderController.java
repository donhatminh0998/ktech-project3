package com.example.ktech_project_3.order;

import com.example.ktech_project_3.AuthenticationFacade;
import com.example.ktech_project_3.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthenticationFacade facade;


    @PostMapping("/{productId}")
    public ResponseEntity<?> createOrder(
            @PathVariable
            Long productId,
            @RequestParam
            Integer quantity
    ) {
        String username = facade.findUsername();
        try {
            if (quantity == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Quantity must be greater than 0.");
            }
            OrderDto orderViewDto = orderService.createOrder(productId, username, quantity);
            return ResponseEntity.ok(orderViewDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
