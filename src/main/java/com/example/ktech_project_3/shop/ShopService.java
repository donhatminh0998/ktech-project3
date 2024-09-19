package com.example.ktech_project_3.shop;

import com.example.ktech_project_3.ItemCategory;
import com.example.ktech_project_3.request.dto.OpenRequestDto;
import com.example.ktech_project_3.shop.dto.ProductDto;
import com.example.ktech_project_3.shop.dto.ProductViewDto;
import com.example.ktech_project_3.shop.dto.ShopDto;
import com.example.ktech_project_3.shop.entity.Product;
import com.example.ktech_project_3.shop.entity.ShopEntity;
import com.example.ktech_project_3.shop.repo.ProductRepository;
import com.example.ktech_project_3.shop.repo.ShopRepository;
import com.example.ktech_project_3.user.entity.UserEntity;
import com.example.ktech_project_3.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public ShopDto updateInfoShop(
            OpenRequestDto openRequestDto,
            UserEntity user
    ){
        System.out.println(openRequestDto);
        ShopEntity shop = user.getShop();
        System.out.println(ShopDto.fromEntity(shop));
        if(openRequestDto.getShopName() !=null){
            shop.setShopName(openRequestDto.getShopName());
        }
        if(openRequestDto.getDescription() !=null){
            shop.setDescription(openRequestDto.getDescription());
        }
        if(openRequestDto.getCategory() !=null){
            shop.setCategory(openRequestDto.getCategory());
        }
        System.out.println(ShopDto.fromEntity(shop));
        shopRepository.save(shop);
        System.out.println(shop.toString());
        user.setShop(shop);
        userRepository.save(user);
        System.out.println(user.toString());
        ShopDto dto = ShopDto.fromEntity(shop);
        System.out.println(dto);
        return dto;
    }

    public ProductDto addProduct(
            Long shopId,
            ProductDto dto,
            MultipartFile image
    ){
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow();
        Product product = new Product();
        product.setNameItem(dto.getItem());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());
        if (image != null && !image.isEmpty()) {
            product.setImage(imagePath(shopId,image));
        }
        product.setShop(shop);
        productRepository.save(product);
        shop.getProducts().add(product);
        shopRepository.save(shop);
        return ProductDto.fromEntity(product);
    }
    public ProductDto updateProduct(
            Long shopId,
            Long productId,
            ProductDto dto,
            MultipartFile image
    ){
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        if( dto.getItem() !=null){
            product.setNameItem(dto.getItem());
        }
        if(dto.getPrice()!=null){
            product.setPrice(dto.getPrice());
        }
        if(dto.getStock()!=null){
            product.setStock(dto.getStock());
        }
        if(dto.getDescription()!=null){
            product.setDescription(dto.getDescription());
        }
        if (image != null && !image.isEmpty()) {
            product.setImage(imagePath(shopId,image));
        }
        product.setShop(shop);
        productRepository.save(product);
        shop.getProducts().add(product);
        shopRepository.save(shop);
        return ProductDto.fromEntity(product);
    }
    public String deleteProduct(
            Long shopId,
            Long productId
    ){
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow();

        if (!productRepository.existsById(productId)) {
            return  "Product not found";
        }
        productRepository.deleteById(productId);

        if(!(productRepository.existsById(productId))){
            return "Product deleted successfully";
        }
        return "Failed to delete the product.";
    }

    public String imagePath (
            Long shopId,
            MultipartFile image
    ){
        String timeString = String.valueOf(System.currentTimeMillis());
        ShopEntity shop = shopRepository.findById(shopId).orElseThrow();
        String shopName = shop.getShopName();

        String profileDir = "media/" + shopId + "/";
        try {
            Files.createDirectories(Path.of(profileDir));
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String originalFilename = image.getOriginalFilename();
        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        String uploadPath = profileDir + timeString +"product." + extension;                 // extension;
        try {
            image.transferTo(Path.of(uploadPath));
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String reqPath = "/static/"+ shopId + "/" +timeString + "product." +extension;               // + extension;
        return reqPath;
    }

    public List<ShopDto> shopsView(
            String shopName,
            ItemCategory category
    ){
        List<ShopDto> shopDtos = new ArrayList<>();
        if((shopName != null) && ( category == null )){
            List<ShopEntity> shops = shopRepository.findShopsByNameOrderedByLatestOrder(shopName);
            for (ShopEntity s : shops){
                shopDtos.add(ShopDto.fromEntity(s));
            }
            return shopDtos;
        }
        else if ((shopName == null) && ( category != null)){
            List<ShopEntity> shops = shopRepository.findShopsByCategoryOrderedByLatestOrder(category);
            for (ShopEntity s : shops){
                shopDtos.add(ShopDto.fromEntity(s));
            }
            return shopDtos;
        }
        else if ((shopName != null) && ( category != null)){
            List<ShopEntity> shops = shopRepository.findShopsByNameAndCategoryOrderedByLatestOrder(shopName,category);
            for (ShopEntity s : shops){
                shopDtos.add(ShopDto.fromEntity(s));
            }
            return shopDtos;
        }
        List<ShopEntity> shops = shopRepository.findAllShopsOrderedByLatestOrder();
        for (ShopEntity s : shops){
            shopDtos.add(ShopDto.fromEntity(s));
        }
        return shopDtos;
    }
    public List<ProductViewDto> productsView(
            String nameItem,
            Integer minPrice,
            Integer maxPrice
    ){
        if(minPrice > maxPrice){
            int m  = minPrice;
            minPrice = maxPrice;
            System.out.println(maxPrice);
            maxPrice = m;
        }
        System.out.println(maxPrice);
        System.out.println(minPrice);

        List<ProductViewDto> proViews =new ArrayList<>();
        if(nameItem != null){
            List<Product> products = productRepository
                    .findProductsByShopAndNameAndPriceRange(nameItem,minPrice,maxPrice);
            for (Product p : products){
                proViews.add(ProductViewDto.fromEntity(p));
            }
            return proViews;
        }
        else{
            List<Product> products = productRepository
                    .findProductsByShopAndPriceRange(minPrice,maxPrice);
            for (Product p : products){
                proViews.add(ProductViewDto.fromEntity(p));
            }
            return proViews;
        }
    }











}
