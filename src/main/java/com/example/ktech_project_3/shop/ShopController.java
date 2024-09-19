package com.example.ktech_project_3.shop;

import com.example.ktech_project_3.AuthenticationFacade;
import com.example.ktech_project_3.ItemCategory;
import com.example.ktech_project_3.request.dto.OpenRequestDto;
import com.example.ktech_project_3.shop.dto.ProductDto;
import com.example.ktech_project_3.shop.dto.ProductViewDto;
import com.example.ktech_project_3.shop.dto.ShopDto;
import com.example.ktech_project_3.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("shops")
public class ShopController {
    private final ShopService shopService;
    private final AuthenticationFacade facade;

    @PutMapping("/update")
    public ResponseEntity<?> infoUpdate(
            @RequestBody
            OpenRequestDto dto
    ){
        UserEntity user = facade.findUser();
        try {
            ShopDto shopDto = shopService.updateInfoShop(dto,user);
            return ResponseEntity.ok(shopDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(
            @RequestParam String nameItem,
            @RequestParam Integer price,
            @RequestParam Integer stock,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image
    ){
        UserEntity user = facade.findUser();
        Long shopId = user.getShop().getId();
        ProductDto dto = new ProductDto();
        dto.setItem(nameItem);
        dto.setPrice(price);
        dto.setStock(stock);
        dto.setDescription(description);
        try {
            ProductDto newProduct = shopService.addProduct(shopId,dto,image);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @PutMapping("/{productId}")
    public ProductDto updateProduct(
            @PathVariable  Long productId,
            @RequestParam(required = false) String nameItem,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image
    ){
        UserEntity user = facade.findUser();
        Long shopId = user.getShop().getId();
        ProductDto dto = new ProductDto();
        dto.setItem(nameItem);
        dto.setPrice(price);
        dto.setStock(stock);
        dto.setDescription(description);

        return  shopService.updateProduct(shopId,productId,dto,image);
    }
    @DeleteMapping("/{productId}")
    public String deleteProduct(
            @PathVariable
            Long productId
    ){
        UserEntity user = facade.findUser();
        Long shopId = user.getShop().getId();
        return shopService.deleteProduct(shopId,productId);
    }
    @GetMapping("/view")
    public List<ShopDto> shops(
            @RequestParam(name = "nameShop", required = false)
            String shopName,
            @RequestParam(name = "category", required = false)
            ItemCategory category
    ){
        return shopService.shopsView(shopName,category);
    }
    @GetMapping("/products")
    public List<ProductViewDto> productsView(
            @RequestParam(name = "nameItem",required = false )
            String nameItem,
            @RequestParam(name ="minPrice", defaultValue = "0")
            Integer minPrice,
            @RequestParam(name = "maxPrice",defaultValue = "100000000")
            Integer maxPrice
    ){
        return shopService.productsView(nameItem,minPrice,maxPrice);
    }
}
