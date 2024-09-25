package com.example.ktech_project_3.user;

import com.example.ktech_project_3.AuthenticationFacade;
import com.example.ktech_project_3.jwt.dto.JwtRequestDto;
import com.example.ktech_project_3.jwt.dto.JwtResponseDto;
import com.example.ktech_project_3.user.dto.RegisterUserDto;
import com.example.ktech_project_3.user.dto.UserDto;
import com.example.ktech_project_3.user.dto.UserProfileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    // private final CustomUserDetailsService detailsService;
    private final AuthenticationFacade authentication;

    public UserController(UserService userService,
                          AuthenticationFacade authentication
                          //  CustomUserDetailsService detailsService
    ) {
        this.userService = userService;
        this.authentication = authentication;
        //      this.detailsService = detailsService;

    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserDto dto) {
        try {
            userService.registerUser(dto);
            return ResponseEntity.ok("User registered successfully.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody
            JwtRequestDto requestDto) {
        try {
            JwtResponseDto responseDto = userService.loginUser(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("users/profile")
    public ResponseEntity<?> profile() {
        String username = authentication.findUsername();
        try {
            UserDto userDto = userService.profile(username);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("users/updateProfile")
    public ResponseEntity<String> updateProfile(
            @RequestBody
            UserProfileDto profileDto
    ) {
        String username = authentication.findUsername();
        try {
            userService.updateProfile(username, profileDto);
            return ResponseEntity.ok("User information updated successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Failed to update user information");
        }
    }

    @PutMapping("users/updateImage")
    public ResponseEntity<?> updateImage(
            @RequestParam
            MultipartFile image
    ) {
        String username = authentication.findUsername();
        log.info("Request to upload profile img for user: {}", username);
        log.info("File name: {}", image.getOriginalFilename());

        try {
            UserDto updateUser = userService.updateImage(username, image);
            return ResponseEntity.ok(updateUser);
        } catch (ResponseStatusException e) {
            log.error("Error updating profile image: {}", e.getReason(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


//    @GetMapping("my-profile")
//    public String myProfile(
//            Authentication authentication
//    ) {
//        log.info(authentication.getName());
//        if (authentication.getPrincipal() instanceof CustomerUserDetails details) {
//            log.info(details.getNickname());
//            log.info(String.valueOf(details.getAge()));
//            log.info(details.getEmail());
//            log.info(details.getPhone());
//            log.info(details.getProfileImage());
//            log.info(details.getUserRole());
//        }
//        return "my-profile";
//    }

//    @GetMapping("register")
//    public String signUpForm() {
//        return "register-form";
//    }


}