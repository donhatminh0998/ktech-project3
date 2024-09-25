package com.example.ktech_project_3.order;

import com.example.ktech_project_3.AuthenticationFacade;
import com.example.ktech_project_3.order.dto.OrderDto;
import com.example.ktech_project_3.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final AuthenticationFacade facade;


    @PostMapping("/orders/{productId}")
    public ResponseEntity<?> createOrder(
            @PathVariable
            Long productId,
            @RequestParam
            Integer quantity
    ){
        String username = facade.findUsername();
        try {
            if (quantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Quantity must be greater than 0.");
            }
            OrderDto orderViewDto = orderService.createOrder(productId,username,quantity);
            return ResponseEntity.ok(orderViewDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/orders/cancel/{orderId}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable
            Long orderId
    ){
        String username = facade.findUsername();
        try {
            OrderDto orderViewDto = orderService.cancelOrder(username,orderId);
            return ResponseEntity.ok(orderViewDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    @PostMapping("/shop/accept/{orderId}")
    public ResponseEntity<?> acceptOrder(
            @PathVariable
            Long orderId
    ){
        String username = facade.findUsername();
        try {
            OrderDto orderViewDto = orderService.acceptOrder(orderId,username);
            return ResponseEntity.ok(orderViewDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/orders/view/{orderId}")
    public ResponseEntity<?> viewOneOrder(
            @PathVariable
            Long orderId
    ) {
        String username = facade.findUsername();
        try {
            OrderDto orderViewDto = orderService.viewOneOrder(orderId, username);
            return ResponseEntity.ok(orderViewDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/orders/userView")
    public List<OrderDto> userView(){
        UserEntity user = facade.findUser();
        return orderService.userView(user);
    }
    @GetMapping("/shop/orderView")
    public List<OrderDto> shopView(){
        UserEntity user = facade.findUser();
        return orderService.shopView(user);
    }

}