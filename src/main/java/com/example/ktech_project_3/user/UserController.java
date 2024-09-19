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

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService service;
    private final CustomerUserDetailsService detailsService;
    private final AuthenticationFacade authentication;


    public UserController(
        UserService service,
        CustomerUserDetailsService detailsService,
        AuthenticationFacade authentication
    ) {
        this.service = service;
        this.detailsService = detailsService;
        this.authentication = authentication;
    }


    // LOGIN
    @GetMapping("login")
    public JwtResponseDto loginUser(
            @RequestBody
            JwtRequestDto dto
    ) {
        return service.loginUser(dto);
    }

    // CREATE
    @PostMapping("register")
    public ResponseEntity<String> registerUser (
            @RequestBody
            RegisterUserDto dto
    ) {
        try {
            service.registerUser(dto);
            return ResponseEntity.ok("User registered successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // UPDATE
    @PutMapping("/{username}/updateProfile")
    public ResponseEntity<String> updateProfile (
            @PathVariable
            String username,
            @RequestBody
            UserProfileDto profileDto
    ) {
        String username1 = authentication.findUsername();
        try {
            service.updateProfile(username1, profileDto);
            return ResponseEntity.ok("User information updated successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Failed to update user information"
            );
        }
    }

    // UPDATE IMAGE
    @PutMapping("/{username}/updateImage")
    public ResponseEntity<?> updateImage (
            @PathVariable
            String username,
            @RequestParam
            MultipartFile image
    ) {
        log.info("Request to upload profile image for user: {}", username);
        log.info("File name: {}", image.getOriginalFilename());

        String username1 = authentication.findUsername();
        try {
            UserDto updateUser = service.updateImage(username, image);
            return ResponseEntity.ok(updateUser);
        } catch (ResponseStatusException e) {
            log.error("Error updating profile image: {}", e.getReason(),e);
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
