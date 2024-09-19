package com.example.ktech_project_3.user;

import com.example.ktech_project_3.jwt.JwtTokenUtils;
import com.example.ktech_project_3.jwt.dto.JwtRequestDto;
import com.example.ktech_project_3.jwt.dto.JwtResponseDto;
import com.example.ktech_project_3.user.dto.RegisterUserDto;
import com.example.ktech_project_3.user.dto.UserDto;
import com.example.ktech_project_3.user.dto.UserProfileDto;
import com.example.ktech_project_3.user.entity.UserEntity;
import com.example.ktech_project_3.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository repository;
    private final CustomerUserDetailsService customerUserDetailsService;
    public UserService(
            PasswordEncoder passwordEncoder,
            JwtTokenUtils jwtTokenUtils,
            UserRepository repository,
            CustomerUserDetailsService customerUserDetailsService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
        this.repository = repository;
        this.customerUserDetailsService = customerUserDetailsService;

        if (repository.findByUsername("admin").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setPhone("010-123-4567");
            admin.setUserRole("ROLE_ADMIN,READ,INSPECT");
            repository.save(admin);
        }

        if (repository.findByUsername("user1").isEmpty()) {
            UserEntity user1 = new UserEntity();
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.setName("Mike");
            user1.setEmail("user1@example.com");
            user1.setPhone("010-333-4444");
            user1.setUserRole("ROLE_USER,READ,ORDER");
            repository.save(user1);
        }

        if (repository.findByUsername("user2").isEmpty()) {
            UserEntity user2 = new UserEntity();
            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("user234"));
            user2.setName("Nick");
            user2.setEmail("user2@example.com");
            user2.setPhone("010-222-3333");
            user2.setUserRole("ROLE_INACTIVE,READ");
            repository.save(user2);
        }
    }

    // CREATE
    public void registerUser(RegisterUserDto dto) {
        if (customerUserDetailsService.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already existed");
        if (!dto.getPassword().equals(dto.getPasswordCheck()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password do not match");
        UserEntity newUser = new UserEntity();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setUserRole("ROLE_INACTIVE,READ");
        repository.save(newUser);
    }

    // LOGIN
    public JwtResponseDto loginUser(
            JwtRequestDto requestDto
    ) {
        if (requestDto.getUsername() == null || requestDto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please check your username and password again");
        }
        UserDetails userDetails;
        try {
            userDetails = customerUserDetailsService.loadUserByUsername(requestDto.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found");
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto responseDto = new JwtResponseDto();
        responseDto.setToken(jwt);
        return responseDto;
    }

    // UPDATE
    public UserDto updateProfile (
            String username,
            UserProfileDto profileDto
    ) {
        Optional<UserEntity> optionalUser = repository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            System.out.println("username not exists");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity user = optionalUser.get();
        if (profileDto.getName() != null) {
            user.setName(profileDto.getName());
        }
        if (profileDto.getAge() != null) {
            user.setAge(Integer.valueOf(profileDto.getAge()));
        }
        if (profileDto.getEmail() != null) {
            user.setEmail(profileDto.getEmail());
        }
        if (profileDto.getPhoneNumber() != null) {
            user.setPhone(profileDto.getPhoneNumber());
        }
        repository.save(user);
        if (user.getName() != null
                && user.getAge() != null
                && user.getEmail() != null
                && user.getPhone() != null
                && Objects.equals(user.getUserRole(), "ROLE_INACTIVE, READ"
        )) {
            user.setUserRole("ROLE_USER, READ, ORDER");
        }
        repository.save(user);
        return UserDto.fromEntity(user);
    }


    // UPDATE IMAGE
    public UserDto updateImage(
            String username,
            MultipartFile image
    ) {
        Optional<UserEntity> optionalUser = repository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            System.out.println("username not exists");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String profileDir = "media/" + username + "/";
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        String uploadPath = profileDir + "profile." + extension;
        try {
            image.transferTo(Path.of(uploadPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String reqPath = "/static/" + username + "/profile." + extension;
        UserEntity target = optionalUser.get();

        target.setProfileImage(reqPath);

        return UserDto.fromEntity(repository.save(target));
    }



//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserEntity> optionalUser = repository.findByUsername(username);
//        if (optionalUser.isEmpty())
//            throw new UsernameNotFoundException(username);
//        return CustomerUserDetails.fromEntity(optionalUser.get());
//    }

//    public UserEntity createUser(String username, String password, String passCheck) {
//        if (userExist(username) || !password.equals(passCheck))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        UserEntity newUser = new UserEntity();
//        newUser.setUsername(username);
//        newUser.setPassword(passwordEncoder.encode(password));
//        newUser.setUserRole("ROLE_USER,READ");
////        repository.save(newUser);
//        return repository.save(newUser);
//    }
//
//    public UserEntity createUser(UserEntity user) {
//        if (userExist(username) || !password.equals(passCheck))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        UserEntity newUser = new UserEntity();
//        newUser.setUsername(user.getUsername());
//        newUser.setPassword(user.getPassword());
//        newUser.setUserRole("ROLE_USER,READ");
//        repository.save(newUser);
//        return repository.save(newUser);
//    }
//
//    public boolean userExist(String username) {
//        return repository.existsByUsername(username);
//    }
}
